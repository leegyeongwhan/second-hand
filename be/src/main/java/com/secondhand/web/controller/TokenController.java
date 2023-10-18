package com.secondhand.web.controller;

import com.secondhand.infrastructure.jwt.JwtTokenProvider;
import com.secondhand.presentation.suport.LoginCheck;
import com.secondhand.presentation.suport.LoginValue;
import com.secondhand.domain.member.Member;
import com.secondhand.domain.member.MemberRepository;
import com.secondhand.domain.memberToken.MemberToken;
import com.secondhand.domain.memberToken.MemberTokenRepository;
import com.secondhand.exception.token.TokenException;
import com.secondhand.exception.MemberNotFoundException;
import com.secondhand.util.BasicResponse;
import com.secondhand.web.dto.response.ResponseTokens;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(tags = "토큰")
@RestController
@RequestMapping("/api/tokens")
@RequiredArgsConstructor
public class TokenController {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final MemberTokenRepository memberTokenRepository;

    @Operation(
            summary = "만료된 토큰 재발급", description = "만료된 토큰을 재발급 받는다."
    )
    @LoginCheck
    @GetMapping("/refresh")
    public BasicResponse<ResponseTokens> reissueRefreshToken(@LoginValue long userId) {
        Member member = memberRepository.findById(userId).orElseThrow(MemberNotFoundException::new);
        ResponseTokens responseTokens = new ResponseTokens(jwtTokenProvider.createToken(member.getId()));
        MemberToken memberToken = memberTokenRepository.findByMemberId(member.getId()).orElseThrow(TokenException::new);
        memberToken.update(responseTokens.getToken().getRefreshToken(), member);
        return BasicResponse.send(HttpStatus.OK.value(),"토큰 재발급", responseTokens);
    }
}
