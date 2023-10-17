package com.secondhand.domain.oauth;

import com.secondhand.domain.login.OAuthRequester;
import com.secondhand.exception.v2.ErrorMessage;
import com.secondhand.exception.v2.NotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum OAuthProvider {

    KAKAO("kakao"),
    GITHUB("github");

    private final String name;
    private OAuthRequester oAuthRequester;

    public static OAuthProvider of(final String name) {
        return Arrays.stream(OAuthProvider.values())
                .filter(provider -> provider.name.equals(name))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(ErrorMessage.OAUTH_PROVIDER_NOT_FOUND));
    }

    private void injectOAuthClient(OAuthRequester oAuthRequester) {
        this.oAuthRequester = oAuthRequester;
    }

    @RequiredArgsConstructor
    @Component
    static class OAuthClientInjector {

 //       private final GithubRequester githubRequester;
        private final KakaoRequester kakaoRequester;

        @PostConstruct
        public void injectOAuthClient() {
            Arrays.stream(OAuthProvider.values()).forEach(oAuthProvider -> {
//                if (oAuthProvider == GITHUB) {
//                    oAuthProvider.injectOAuthClient(githubRequester);
//                }
                if (oAuthProvider == KAKAO) {
                    oAuthProvider.injectOAuthClient(kakaoRequester);
                }
            });
        }
    }
}
