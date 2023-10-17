package com.secondhand.domain.login;

import com.secondhand.exception.v2.ErrorMessage;
import com.secondhand.exception.InternalServerException;
import com.secondhand.web.dto.login.UserProfile;
import com.secondhand.web.dto.response.OauthTokenResponse;

import java.util.Map;

public interface OAuthRequester {

    OauthTokenResponse getToken(String code);

    UserProfile getUserProfile(OauthTokenResponse tokenResponse);

    default void validateToken(Map<String, Object> tokenResponse) {
        if (!tokenResponse.containsKey("access_token")) {
            throw new InternalServerException(
                    ErrorMessage.OAUTH_FAIL_REQUEST_TOKEN,
                    tokenResponse.get("error_description").toString()
            );
        }
    }
}

