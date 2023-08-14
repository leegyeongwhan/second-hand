package com.secondhand.web.controller;

import com.secondhand.domain.chat.ChatMessage;
import com.secondhand.domain.chat.RedisPublisher;
import com.secondhand.domain.chat.dto.request.ChatRequest;
import com.secondhand.domain.member.Member;
import com.secondhand.exception.MemberNotFoundException;
import com.secondhand.service.ChatService;
import com.secondhand.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chats")
public class ChatController {

    private final RedisPublisher redisPublisher;
    private final ChannelTopic channelTopic;

    @MessageMapping("/message")
    public void chat(@Valid ChatMessage chatRequest) {
        log.debug("pub controller");
        redisPublisher.publish(channelTopic.getTopic(), chatRequest);
    }
}
