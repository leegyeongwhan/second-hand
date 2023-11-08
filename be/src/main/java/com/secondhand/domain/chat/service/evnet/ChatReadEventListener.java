package com.secondhand.domain.chat.service.evnet;

import com.secondhand.domain.chat.repository.ChatLogRepository;
import com.secondhand.web.dto.chat.event.ChatReadEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
@Component
public class ChatReadEventListener {

    private final ChatLogRepository chatLogRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener
    public void changeReadStatusOfChatLog(ChatReadEvent event) {
        chatLogRepository.updateReadCountByChatRoomId(event.getChatRoomId(), event.getReaderId());
    }
}
