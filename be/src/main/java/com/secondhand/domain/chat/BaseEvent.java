package com.secondhand.domain.chat;

import lombok.Getter;

import java.time.Instant;

@Getter
public abstract class BaseEvent {
    private Instant createdAt;

    protected BaseEvent() {
        this.createdAt = Instant.now();
    }
}
