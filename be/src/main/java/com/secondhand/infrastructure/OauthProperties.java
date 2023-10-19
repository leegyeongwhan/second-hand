package com.secondhand.infrastructure;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@ConfigurationProperties(prefix = "auth2")
public class OauthProperties {

    private final Github github;
    private final Kakao kakao;

    @ConstructorBinding
    public OauthProperties(Github github, Kakao kakao) {
        this.github = github;
        this.kakao = kakao;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Github {

        private final User user;
        private final Provider provider;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Kakao {

        private final User user;
        private final Provider provider;
    }

    @Getter
    @RequiredArgsConstructor
    public static class User {

        private final String clientId;
        private final String clientSecret;
        private final String redirectUrl;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Provider {

        private final String tokenUrl;
        private final String userInfoUrl;
        private final String userNameAttribute;
    }
}

