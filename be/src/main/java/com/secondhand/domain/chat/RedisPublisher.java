package com.secondhand.domain.chat;

import com.secondhand.domain.chat.dto.request.ChatRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Getter
@RequiredArgsConstructor
public class RedisPublisher {
    private final ApplicationEventPublisher eventPublisher;
    private final RedisTemplate<String, Object> redisTemplate;

    @Transactional
    public void publish(String topic, ChatMessage chatMessage) {
        log.debug("pub log : " + chatMessage.toString() + "/ topic: " + topic);
        //    chatMessage.ready();
        redisTemplate.convertAndSend(topic, chatMessage);

        eventPublisher.publishEvent(new ChatMessageEvent(chatMessage));
    }
}
