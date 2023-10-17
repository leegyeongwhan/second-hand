package com.secondhand.domain.oauth.dto.req;

import com.secondhand.domain.oauth.OAuthProvider;
import com.secondhand.domain.oauth.OAuthProviderV1;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoRequestCode implements OAuthLoginParams {
    private String authorizationCode;

    @Override
    public OAuthProviderV1 oAuthProvider() {
        return OAuthProviderV1.KAKAO;
    }
}
