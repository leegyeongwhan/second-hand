package com.secondhand.web.dto.login.response;

import lombok.Getter;

@Getter
public class UserResponse {

    private final String loginId;
    private final String profileUrl;

    public UserResponse(String loginId, String profileUrl) {
        this.loginId = loginId;
        this.profileUrl = profileUrl;
    }
}

