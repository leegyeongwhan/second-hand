package com.secondhand.web.dto.chat.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ChatReadEvent {

    private final Long chatRoomId;
    private final Long readerId;
}
