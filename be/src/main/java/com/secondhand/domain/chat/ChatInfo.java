package com.secondhand.domain.chat;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class ChatInfo implements Serializable {

    private static final long serialVersionUID = 6494678977089006639L;

    private String roomId;
    private String name;
    private Long userCount; // 채팅방 인원수
}
