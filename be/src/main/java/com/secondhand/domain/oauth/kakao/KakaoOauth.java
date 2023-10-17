package com.secondhand.domain.oauth.kakao;

import com.secondhand.domain.oauth.OAuthProviderV1;
import com.secondhand.domain.oauth.Oauth;
import com.secondhand.domain.oauth.dto.KakaoInfoResponse;
import com.secondhand.domain.oauth.dto.OAuthInfoResponse;
import com.secondhand.domain.oauth.dto.req.KakaoRequestCode;
import com.secondhand.domain.oauth.dto.req.OAuthLoginParams;
import com.secondhand.exception.ouath.AccessTokenNotFoundException;
import com.secondhand.exception.ouath.GitHubRequestException;
import com.secondhand.exception.ouath.GitHubUserInfoNotFoundException;
import com.secondhand.exception.ouath.KakoRequestException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@Component
@RequiredArgsConstructor
public class KakaoOauth implements Oauth {

    private final WebClient webClient;
    private final Logger logger = LoggerFactory.getLogger(KakaoOauth.class);

    @Value("${OAUTH_KAKAO_CLIENT_API_URL}")
    private String apiUrl;
    @Value("${OAUTH_KAKAO_CLIENT_AUTH_URL}")
    private String authUrl;

    @Value("${OAUTH_KAKAO_REDIRECT_URL}")
    private String redirectUrl;
    @Value("${OAUTH_KAKAO_CLIENT_ID}")
    private String clientId;

    @Override
    public OAuthProviderV1 oAuthProvider() {
        return OAuthProviderV1.KAKAO;
    }

    @Override
    public String getToken(OAuthLoginParams params, String userAgentDTO) {
        String code = ((KakaoRequestCode) params).getAuthorizationCode();

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUrl);
        body.add("code", code);

        logger.debug("body = {}", body);

        KakoTokens kakoTokens = webClient.post()
                .uri(apiUrl)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .bodyValue(body)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, error -> Mono.error(KakoRequestException::new))
                .bodyToMono(KakoTokens.class)
                .blockOptional()
                .orElseThrow(AccessTokenNotFoundException::new);

        logger.debug("accessTokenResponseDTO = {}", kakoTokens);

        return kakoTokens.getAccessToken();
    }

    @Override
    public OAuthInfoResponse getUserInfo(String accessToken) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("property_keys", "[\"kakao_account.email\", \"kakao_account.profile\"]");

        return webClient.post()
                .uri(authUrl)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer" + " " + accessToken)
                .bodyValue(body)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, error -> Mono.error(GitHubRequestException::new))
                .bodyToMono(KakaoInfoResponse.class)
                .blockOptional()
                .orElseThrow(GitHubUserInfoNotFoundException::new);
    }
}
