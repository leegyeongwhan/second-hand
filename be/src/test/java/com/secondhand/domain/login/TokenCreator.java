package com.secondhand.domain.login;


import com.secondhand.infrastructure.jwt.JwtTokenProvider;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class TokenCreator {

    private static final String secretKey = "fsdfsdfsdfsdffsdsd";
    private final JwtTokenProvider jwtProvider;

    TokenCreator(JwtTokenProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    public Token createToken(Long payload) {
        return jwtProvider.createToken(payload);
    }

    public static String createExpiredToken(Long payload) {
        Date now = new Date();
        return Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() - 1))
                .signWith(SignatureAlgorithm.HS256, secretKey) // HS256 알고리즘과 시크릿 키를 사용하여 서명
                .compact();
    }
}

