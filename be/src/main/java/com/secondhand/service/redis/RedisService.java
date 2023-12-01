package com.secondhand.service.redis;

import com.secondhand.domain.chat.ChatBubble;
import com.secondhand.domain.chat.ChatBubbleArrivedEvent;
import com.secondhand.domain.chat.ChatLog;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ApplicationEventPublisher publisher;

    public void set(String key, String value, long expiration) {
        redisTemplate.opsForValue().set(key, value, expiration, TimeUnit.MILLISECONDS);
    }

    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    //TODO: 리스너 추가
    @Transactional
    public void publish(String topic, ChatBubble message) {
        message.ready(); // id, createdAt 설정
        redisTemplate.convertAndSend(topic, message);

        publisher.publishEvent(new ChatBubbleArrivedEvent(message));
    }
}
