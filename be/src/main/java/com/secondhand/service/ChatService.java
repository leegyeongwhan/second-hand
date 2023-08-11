package com.secondhand.service;

import com.secondhand.domain.chat.ChatRepository;
import com.secondhand.domain.chat.dto.request.ChatRequest;
import com.secondhand.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;

    @Transactional
    public void sendMessage(ChatRequest chatRequest, Member member) {
//        ChatRoom chatRoom = chatRepository.findById(chatRequest.getRoomId()).orElseThrow(ChatRoomNotFoundException::new);
//
//        //채팅 생성 및 저장
//        ChatMessage chatMessage = ChatMessage.builder()
//                .chatRoom(chatRoom)
//                .user(member)
//                .message(chatRequest.getMessage())
//                .build();
//
//        chatMessageRepository.save(chatMessage);
//        String topic = channelTopic.getTopic();
//
//        // ChatMessageRequest에 유저정보, 현재시간 저장
//        chatRequest.setNickName(member.getNickname());
//        chatRequest.setUserId(member.getId());
//
//        if (chatRequest.getType() == ChatMessageRequest.MessageType.TALK) {
//            // 그륩 채팅일 경우
//            redisTemplate.convertAndSend(topic, chatRequest);
//        }
    }
}
