package com.secondhand.web.dto.login.response;

import lombok.Getter;

@Getter
public class UserResponse {

    private final String loginId;
    private final String profileUrl;
    private final String oAuthProvider;

    public UserResponse(String loginId, String profileUrl, String oAuthProvider) {
        this.loginId = loginId;
        this.profileUrl = profileUrl;
        this.oAuthProvider = oAuthProvider;
    }
}

