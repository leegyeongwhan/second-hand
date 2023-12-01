package com.secondhand.domain.chat;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class ChatBubbleArrivedEvent extends BaseEvent {
    private final ChatBubble chatBubble;

    public ChatBubbleArrivedEvent(ChatBubble chatBubble) {
        super();
        this.chatBubble = chatBubble;
        log.info("ChatBubble Arrived Event Occur = {}", chatBubble.toString());
    }

    public Long getChatReceiverId() {
        return chatBubble.getReceiver();
    }
}
