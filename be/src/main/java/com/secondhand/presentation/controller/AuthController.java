package com.secondhand.presentation.controller;

import com.secondhand.domain.oauth.OAuthProvider;
import com.secondhand.presentation.dto.token.AccessTokenResponse;
import com.secondhand.presentation.dto.token.LogoutRequest;
import com.secondhand.presentation.dto.token.TokenRenewRequest;
import com.secondhand.presentation.support.NotNullParam;
import com.secondhand.service.AuthService;
import com.secondhand.service.TokenService;
import com.secondhand.util.BasicResponse;
import com.secondhand.web.dto.login.request.LoginRequest;
import com.secondhand.web.dto.login.request.SignUpRequest;
import com.secondhand.web.dto.login.response.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/api/auth")
@RestController
public class AuthController {

    private final AuthService authService;
    private final TokenService tokenService;

    @PostMapping("/{provider}/login")
    public BasicResponse<LoginResponse> login(@PathVariable OAuthProvider provider,
                                              @RequestBody @Valid LoginRequest request,
                                              @NotNullParam(message = "code 값은 반드시 들어와야 합니다.") String code) {
        LoginResponse login = authService.login(provider, request, code);
        return BasicResponse.send(HttpStatus.OK.value(), "카카오 로그인", login);
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping(value = "/{provider}/signup")
    public BasicResponse<Valid> signUp(@RequestHeader(name = "User-Agent") String userAgent,
                                       @PathVariable OAuthProvider provider,
                                       @NotNullParam(message = "code 값은 반드시 들어와야 합니다.") String code,
                                       @RequestBody @Valid SignUpRequest signupData) {
        authService.signUp(provider, signupData, code, userAgent);
        return BasicResponse.send(HttpStatus.CREATED.value(), "소셜 가입");
    }

    @PostMapping("/token")
    public BasicResponse<AccessTokenResponse> renewAccessToken(@Valid @RequestBody TokenRenewRequest request) {
        return BasicResponse.send(HttpStatus.OK.value(), "토큰 재요청", tokenService.renewAccessToken(request.getRefreshToken()));
    }

    @PostMapping("/logout")
    public BasicResponse<Void> logout(HttpServletRequest request,
                                    @Valid @RequestBody LogoutRequest logoutRequest) {
        authService.logout(request, logoutRequest.getRefreshToken());
        return BasicResponse.send(HttpStatus.OK.value(), "로그 아웃");

    }
}
