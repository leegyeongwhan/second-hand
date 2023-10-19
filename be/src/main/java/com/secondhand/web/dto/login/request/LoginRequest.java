package com.secondhand.web.dto.login.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class LoginRequest {

    @NotBlank
    @Size(min = 2, max = 12, message = "아이디는 2자 ~ 12자여야 합니다.") // todo: ExceptionHandler에서 예외처리하기
    private String loginName;

    public LoginRequest(String loginName) {
        this.loginName = loginName;
    }
}
