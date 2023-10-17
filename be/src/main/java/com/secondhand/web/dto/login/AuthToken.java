package com.secondhand.web.dto.login;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthToken {

    private final String accessToken;
    private final String refreshToken;
}

