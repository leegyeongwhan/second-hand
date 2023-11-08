package com.secondhand.web.dto.chat;

import com.secondhand.domain.chat.ChatLog;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SimpleChatLog {

    private Long messageId;
    private Boolean isMe;
    private String message;

    public static SimpleChatLog of(ChatLog chatLog, boolean isMe) {
        return new SimpleChatLog(chatLog.getId(), isMe, chatLog.getMessage());
    }
}