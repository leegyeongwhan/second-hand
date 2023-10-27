package com.secondhand.presentation.dto.token;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class LogoutRequest {

    @NotBlank(message = "토큰 값은 비어 있을 수 없습니다.")
    private String refreshToken;
}
