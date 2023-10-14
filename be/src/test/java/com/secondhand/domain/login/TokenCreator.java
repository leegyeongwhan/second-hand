package com.secondhand.domain.login;


import com.secondhand.infrastructure.jwt.JwtProperties;
import com.secondhand.infrastructure.jwt.JwtTokenProvider;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.util.Date;

public class TokenCreator {

    private static final String secretKey = "fsdfsdfsdfsdgfdgdgfdgfdgfdgfdgfdffsdsd";
    private static final JwtTokenProvider jwtProvider = new JwtTokenProvider(new JwtProperties(secretKey));

    public static Token createToken(Long payload) {
        return jwtProvider.createToken(payload);
    }

    public static String createExpiredToken(Long payload) {
        Date now = new Date();
        return Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() - 1))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();
    }
}

