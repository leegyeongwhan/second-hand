package com.secondhand.domain.oauth;

import com.secondhand.domain.oauth.dto.OAuthInfoResponse;
import com.secondhand.domain.oauth.dto.req.OAuthLoginParams;

public interface Oauth {
    OAuthProviderV1 oAuthProvider();

    String getToken(OAuthLoginParams params, String userAgent);

    OAuthInfoResponse getUserInfo(String accessToken);
}
