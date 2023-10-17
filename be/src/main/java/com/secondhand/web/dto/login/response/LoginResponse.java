package com.secondhand.web.dto.login.response;

import com.secondhand.web.dto.login.AuthToken;
import lombok.Getter;

@Getter
public class LoginResponse {

    private final AuthToken jwt;
    private final UserResponse user;

    public LoginResponse(AuthToken authToken, UserResponse user) {
        this.jwt = authToken;
        this.user = user;
    }
}
