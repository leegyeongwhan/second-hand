package com.secondhand.domain.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomResponse {

    private Long chatRoomId;
    private String thumbnailUrl;
    private String chatPartnerName;
    private String chatPartnerProfile;
    private LocalDateTime lastSendTime;
    private String lastSendMessage;
    private Long newMessageCount;

    public void assignNewMessageCount(Long messageCount) {
        this.newMessageCount = messageCount;
    }
}
