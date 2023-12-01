package com.secondhand.domain.chat.dto.request;

import com.secondhand.domain.chat.ChatLog;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ChatRequest {

    @NotNull(message = "채팅 메시지는 null 일 수 없습니다.")
    private String message;

}
