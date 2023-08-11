package com.secondhand.web.controller;

import com.secondhand.domain.chat.dto.request.ChatRequest;
import com.secondhand.domain.member.Member;
import com.secondhand.exception.MemberNotFoundException;
import com.secondhand.service.ChatService;
import com.secondhand.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    private final ChatService chatService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final MemberService memberService;

//    @MessageMapping("/messages")
//    public void chat(@Valid ChatRequest chatRequest) {
//        chattingService.save(chatRequest);
//        simpMessagingTemplate.convertAndSend("/sub/rooms/" + chatRequest.getRoomId(), chatRequest.getMessage());
//    }

    @MessageMapping("/message")
    public void chat(@Valid ChatRequest chatRequest) {
        Member member = memberService.findMemberById(chatRequest.getSenderId());

        chatService.sendMessage(chatRequest, member);
    }
}
