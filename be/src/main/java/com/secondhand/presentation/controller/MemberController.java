package com.secondhand.presentation.controller;

import com.secondhand.domain.oauth.dto.req.GithubRequestCode;
import com.secondhand.presentation.support.LoginCheck;
import com.secondhand.presentation.support.LoginValue;
import com.secondhand.service.LoginService;
import com.secondhand.service.MemberService;
import com.secondhand.util.BasicResponse;
import com.secondhand.web.dto.requset.JoinRequest;
import com.secondhand.web.dto.requset.SignupSocialRequest;
import com.secondhand.web.dto.requset.UpdateNickNameRequest;
import com.secondhand.web.dto.response.MemberLoginResponse;
import com.secondhand.web.dto.response.MemberResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final LoginService loginService;

    @PostMapping("/auth/github/login")
    public BasicResponse<MemberLoginResponse> login(
            @RequestHeader(name = "User-Agent") String userAgent,
            @RequestBody GithubRequestCode code,
            HttpServletResponse response) throws IOException, InterruptedException {
        log.debug("프론트로 부터받은  코드= {}", code.getAuthorizationCode());
        MemberLoginResponse memberResponseDTO = loginService.login(code, userAgent);
        memberService.setMemberProfileByOauthLogin(memberResponseDTO);
        // jwtService.setTokenHeader(memberProfileResponse, response);

        return BasicResponse.send(HttpStatus.OK.value(), "깃허브 로그인", memberResponseDTO);
    }

//    @Operation(
//            summary = "카카오 로그인", description = "사용자 카카오를 통한 로그인"
//    )
//    @PostMapping("/auth/kakao/login")
//    public BasicResponse<MemberLoginResponse> kakaoLogin(@RequestHeader(name = "User-Agent") String userAgent,
//                                                         @RequestBody KakaoRequestCode params,
//                                                         HttpServletResponse response) throws IOException {
//        log.debug("프론트로 부터 받은 코드 = {}", params.getAuthorizationCode());
//        log.debug("프론트로 부터 받은 코드 = {}", params.oAuthProvider().name());
//        MemberLoginResponse memberResponseDTO = loginService.login(params, userAgent);
//        memberService.setMemberProfileByOauthLogin(memberResponseDTO);
//        // jwtService.setTokenHeader(memberProfileResponse, response);
//
//        return BasicResponse.send(HttpStatus.OK.value(),"카카오 로그인", memberResponseDTO);
//    }


    @PostMapping("/join")
    public BasicResponse<MemberLoginResponse> join(
            final @Valid @RequestBody JoinRequest joinRequest) {
        
        MemberLoginResponse memberResponseDTO = loginService.join(joinRequest);

        return BasicResponse.send(HttpStatus.OK.value(), "일반 회원가입 로그인", memberResponseDTO);
    }

    @LoginCheck
    @PostMapping("/members/signup")
    public BasicResponse<MemberLoginResponse> signupEmail(
            @LoginValue long userId,
            final @Valid @RequestBody SignupSocialRequest signupSocialRequest) {

        MemberLoginResponse memberLoginResponse = loginService.signupEmail(userId,
                signupSocialRequest);

        return BasicResponse.send(HttpStatus.OK.value(), "사용자 이메일 추가", memberLoginResponse);
    }


    @LoginCheck
    @PatchMapping("/members")
    public BasicResponse<String> updateNickName(
            @LoginValue long userId,
            @RequestBody UpdateNickNameRequest nickNameRequest) {
        memberService.updateNickName(userId, nickNameRequest);

        return BasicResponse.send(HttpStatus.OK.value(), "유저닉네임수정");
    }

    @LoginCheck
    @GetMapping("/auth/logout")
    public BasicResponse<String> logout(@LoginValue long userId) {
        log.debug("로그아웃 요청");
        loginService.logout(userId);

        return BasicResponse.send(HttpStatus.OK.value(), "로그아웃 요청");
    }

    @LoginCheck
    @GetMapping("/members")
    public BasicResponse<MemberResponse> info(@LoginValue long userId) {
        log.debug("사용자 id = {} ", userId);
        MemberResponse userInfo = memberService.getUserInfo(userId);

        return BasicResponse.send(HttpStatus.OK.value(), "사용자 정보를 가져온다", userInfo);
    }
}
