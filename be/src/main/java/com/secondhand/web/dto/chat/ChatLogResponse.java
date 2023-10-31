package com.secondhand.web.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ChatLogResponse {

    private String chatPartnerName;
    private ProductSimpleResponse item;
    private List<SimpleChatLog> chat;
    private Long nextMessageId;
}
