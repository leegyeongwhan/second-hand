package com.secondhand.service;

import com.secondhand.domain.product.repository.ProductRepository;
import com.secondhand.util.RedisUtil;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ViewCountService {

    private static final int INITIAL_VIEW_COUNT = 1;

    private final ProductRepository productRepository;
    private final ViewCountRedisService redisService;
    private final RedissonClient redissonClient;

    public void increaseViewCount(Long itemId) {
        String viewCountKey = RedisUtil.createItemViewCountCacheKey(itemId);

        RLock lock = redissonClient.getLock("lock::" + viewCountKey);

        try {
            boolean available = lock.tryLock(10, 1, TimeUnit.SECONDS);
            if (!available) {
                return;
            }

            if (redisService.hasKey(viewCountKey)) {
                redisService.increase(viewCountKey);
                return;
            }

            redisService.set(viewCountKey, INITIAL_VIEW_COUNT, Duration.ofSeconds(100).toMillis());
        } catch (InterruptedException ignored) {
        } finally {
            lock.unlock();
        }
    }

    @Async("viewCountExecutor")
    @Scheduled(fixedDelay = 5000L)
    @Transactional
    public void applyViewCountToRDB() {
        List<String> itemViewCountKeys = redisService.getKeysOrderByExpiration(
                RedisUtil.getProductViewCountCacheKeyPattern());

        if (itemViewCountKeys.isEmpty()) {
            return;
        }
        itemViewCountKeys.forEach(key -> {
            int viewCount = redisService.getAndDelete(key);
            productRepository.findById(extractItemId(key))
                    .ifPresent(product -> product.addViewCount(viewCount));
        });
    }

    private Long extractItemId(String key) {
        return Long.parseLong(key.split(RedisUtil.getKeyDelimiter())[1]);
    }
}