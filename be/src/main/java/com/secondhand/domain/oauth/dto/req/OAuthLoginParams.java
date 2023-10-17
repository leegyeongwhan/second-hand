package com.secondhand.domain.oauth.dto.req;

import com.secondhand.domain.oauth.OAuthProvider;
import com.secondhand.domain.oauth.OAuthProviderV1;

public interface OAuthLoginParams {
    OAuthProviderV1 oAuthProvider();
}
