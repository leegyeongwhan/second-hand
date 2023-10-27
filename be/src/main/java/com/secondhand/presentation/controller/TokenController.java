package com.secondhand.presentation.controller;

import com.secondhand.domain.member.Member;
import com.secondhand.domain.member.MemberRepository;
import com.secondhand.domain.memberToken.MemberTokenRepository;
import com.secondhand.exception.MemberNotFoundException;
import com.secondhand.infrastructure.jwt.JwtTokenProvider;
import com.secondhand.presentation.support.LoginCheck;
import com.secondhand.presentation.support.LoginValue;
import com.secondhand.util.BasicResponse;
import com.secondhand.web.dto.response.ResponseTokens;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/tokens")
@RequiredArgsConstructor
public class TokenController {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final MemberTokenRepository memberTokenRepository;

    @LoginCheck
    @GetMapping("/refresh")
    public BasicResponse<ResponseTokens> reissueRefreshToken(@LoginValue long userId) {
        Member member = memberRepository.findById(userId).orElseThrow(MemberNotFoundException::new);
        ResponseTokens responseTokens = new ResponseTokens(jwtTokenProvider.createToken(member.getId()));
        return BasicResponse.send(HttpStatus.OK.value(),"토큰 재발급", responseTokens);
    }
}
