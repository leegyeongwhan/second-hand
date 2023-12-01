package com.secondhand.domain.chat.dto.request;

import com.secondhand.domain.chat.ChatBubble;

public class ChatbubbleRequest {

    private String chatroomId;
    private Long sender;
    private Long receiver;
    private String message;

    public ChatBubble toDomain() {
        return ChatBubble.builder()
                .chatroomId(chatroomId)
                .sender(sender)
                .receiver(receiver)
                .message(message)
                .build();
    }
}
