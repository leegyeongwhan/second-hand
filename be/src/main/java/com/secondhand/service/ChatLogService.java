package com.secondhand.service;

import com.secondhand.domain.chat.ChatLog;
import com.secondhand.domain.chat.ChatRoom;
import com.secondhand.domain.chat.repository.ChatLogRepository;
import com.secondhand.domain.chat.repository.ChatRoomRepository;
import com.secondhand.domain.member.Member;
import com.secondhand.domain.product.Product;
import com.secondhand.domain.product.repository.ProductRepository;
import com.secondhand.exception.v2.ErrorMessage;
import com.secondhand.exception.v2.NotFoundException;
import com.secondhand.web.dto.chat.ChatLogResponse;
import com.secondhand.web.dto.chat.ProductSimpleResponse;
import com.secondhand.web.dto.chat.SimpleChatLog;
import com.secondhand.web.dto.chat.event.ChatReadEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ChatLogService {

    private final ApplicationEventPublisher eventPublisher;
    private final ChatLogRepository chatLogRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ProductRepository productRepository;

    public ChatLogResponse getMessages(Long chatRoomId, long messageId, Long memberId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));
        Product product = productRepository.findById(chatRoom.getProduct().getId())
                .orElseThrow(() -> NotFoundException.productNotFound(ErrorMessage.NOT_FOUND,
                        chatRoom.getProduct().getId()));

        Member receiver = chatRoom.getSeller();

        List<ChatLog> chatLogs = chatLogRepository.findAllByChatRoom_IdAndIdIsGreaterThan(
                chatRoomId, messageId);
        List<SimpleChatLog> chatLogsResponse = chatLogs.stream()
                .map(chatLog -> SimpleChatLog.of(chatLog, chatLog.getSenderId().equals(memberId)))
                .collect(Collectors.toList());

        eventPublisher.publishEvent(new ChatReadEvent(chatRoomId, memberId));

        Long lastMessageId =
                chatLogs.isEmpty() ? messageId : chatLogs.get(chatLogs.size() - 1).getId();
        return new ChatLogResponse(receiver.getLoginName(), ProductSimpleResponse.from(product),
                chatLogsResponse, lastMessageId);
    }

    @Transactional
    public void sendMessage(String message, Long chatRoomId, Long senderId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));

        ChatLog chatLog = ChatLog.of(chatRoom, message, senderId);
        chatLogRepository.save(chatLog);

        chatRoom.setLastSendMessage(message);
        chatRoom.changeLastSendTime();
        // TODO: 알람 보내기
    }
}
