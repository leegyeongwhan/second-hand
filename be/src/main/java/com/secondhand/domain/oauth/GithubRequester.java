//package com.secondhand.domain.oauth;
//
//import com.secondhand.domain.oauth.dto.AccessTokenResponseDTO;
//import com.secondhand.domain.oauth.dto.GithubInfoResponse;
//import com.secondhand.domain.oauth.dto.OAuthInfoResponse;
//import com.secondhand.domain.oauth.dto.req.AccessTokenRequestBodyDTO;
//import com.secondhand.domain.oauth.dto.req.GithubRequestCode;
//import com.secondhand.domain.oauth.dto.req.OAuthLoginParams;
//import com.secondhand.domain.oauth.github.GitHubOauth;
//import com.secondhand.domain.oauth.service.GiHubService;
//import com.secondhand.exception.ouath.AccessTokenNotFoundException;
//import com.secondhand.exception.ouath.GitHubRequestException;
//import com.secondhand.exception.ouath.GitHubUserInfoNotFoundException;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Component;
//import org.springframework.web.reactive.function.client.WebClient;
//import reactor.core.publisher.Mono;
//
//@Component
//public class GithubRequester implements OAuthRequester {
//    private final GiHubService giHubService;
//    private final WebClient webClient;
//    private final Logger logger = LoggerFactory.getLogger(GithubRequester.class);
//
//    private final java.lang.String url;
//    private final java.lang.String redirectUrl;
//
//    public GithubRequester(GiHubService giHubService,
//                       WebClient webClient,
//                       @Value("${OAUTH_GITHUB_URL}") java.lang.String url,
//                       @Value("${OAUTH_GITHUB_REDIRECT_URL}") java.lang.String redirectUrl) {
//        this.giHubService = giHubService;
//        this.webClient = webClient;
//        this.url = url;
//        this.redirectUrl = redirectUrl;
//    }
//
//    @Override
//    public OAuthProvider oAuthProvider() {
//        return OAuthProvider.GITHUB;
//    }
//
//    @Override
//    public String getToken(OAuthLoginParams params, String userAgent) {
//        String code = ((GithubRequestCode) params).getAuthorizationCode();
//        AccessTokenRequestBodyDTO requestBodyDTO = AccessTokenRequestBodyDTO.builder()
//                .clientId(giHubService.getClientId(userAgent))
//                .clientSecret(giHubService.getClientSecret(userAgent))
//                .code(code)
//                .build();
//        logger.debug("requestBodyDTO = {}", requestBodyDTO);
//
//
//        AccessTokenResponseDTO accessTokenResponseDTO = webClient.post()
//                .uri(url)
//                .accept(MediaType.APPLICATION_JSON)
//                .bodyValue(requestBodyDTO)
//                .retrieve()
//                .onStatus(HttpStatus::is4xxClientError, error -> Mono.error(GitHubRequestException::new))
//                .bodyToMono(AccessTokenResponseDTO.class)
//                .blockOptional()
//                .orElseThrow(AccessTokenNotFoundException::new);
//
//        logger.debug("accessTokenResponseDTO = {}", accessTokenResponseDTO);
//        return accessTokenResponseDTO.getAccessToken();
//    }
//
//    @Override
//    public OAuthInfoResponse getUserInfo(String accessToken) {
//        return webClient.get()
//                .uri(redirectUrl)
//                .accept(MediaType.APPLICATION_JSON)
//                .header(HttpHeaders.AUTHORIZATION, "token" + " " + accessToken)
//                .retrieve()
//                .onStatus(HttpStatus::is4xxClientError, error -> Mono.error(GitHubRequestException::new))
//                .bodyToMono(GithubInfoResponse.class)
//                .blockOptional()
//                .orElseThrow(GitHubUserInfoNotFoundException::new);
//    }
//}
