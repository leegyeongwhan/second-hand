package com.secondhand.domain.chat.service;

import com.secondhand.domain.chat.BaseEvent;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;

@Slf4j
@Getter
public class ChatroomCreatedEvent extends ApplicationEvent {
    private final ChatroomInfo info;

    public ChatroomCreatedEvent(ChatroomInfo info) {
        super(info);
        this.info = info;
        log.info("chatroom created event = {}", info);
    }
}

