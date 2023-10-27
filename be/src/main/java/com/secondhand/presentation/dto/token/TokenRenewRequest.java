package com.secondhand.presentation.dto.token;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TokenRenewRequest {

    @NotBlank(message = "토큰값은 비어있을 수 없습니다.")
    private String refreshToken;
}
