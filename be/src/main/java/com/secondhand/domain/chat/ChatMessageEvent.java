package com.secondhand.domain.chat;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class ChatMessageEvent extends BaseEvent {
    private final ChatMessage chatMessage;

    public ChatMessageEvent(ChatMessage chatMessage) {
        super();
        this.chatMessage = chatMessage;
        log.info("ChatBubble Arrived Event Occur = {}", chatMessage.toString());
    }

    public String getChatReceiverId() {
        return chatMessage.getReceiver();
    }
}
