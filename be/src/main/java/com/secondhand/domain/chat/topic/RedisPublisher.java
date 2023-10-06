package com.secondhand.domain.chat.topic;

import com.secondhand.domain.chat.ChatMessage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Slf4j
@Getter
@RequiredArgsConstructor
@Service
public class RedisPublisher {
    private final RedisTemplate<String, Object> redisTemplate;

    public void publish(ChannelTopic topic, ChatMessage message) {
        System.out.println("topic = " + topic);
        log.debug("pub log : " +  message.toString() + "/ topic: " + topic);
        redisTemplate.convertAndSend(topic.getTopic(), message);
    }
}
