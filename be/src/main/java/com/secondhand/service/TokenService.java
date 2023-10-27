package com.secondhand.service;

import com.secondhand.domain.memberToken.MemberTokenRepository;
import com.secondhand.exception.v2.ErrorMessage;
import com.secondhand.exception.v2.UnAuthorizedException;
import com.secondhand.infrastructure.jwt.JwtTokenProvider;
import com.secondhand.presentation.dto.token.AccessTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class TokenService {

    private final MemberTokenRepository tokenRepository;
    private final JwtTokenProvider jwtProvider;

    public AccessTokenResponse renewAccessToken(final String refreshToken) {
        jwtProvider.validateToken(refreshToken);
        Map<String, Object> claims = jwtProvider.extractClaims(refreshToken);
        Long memberId = Long.valueOf(claims.get("memberId").toString());

        if (!tokenRepository.existsById(memberId)) {
            throw new UnAuthorizedException(ErrorMessage.INVALID_TOKEN);
        }
        return new AccessTokenResponse(jwtProvider.createAccessToken(memberId));
    }
}
