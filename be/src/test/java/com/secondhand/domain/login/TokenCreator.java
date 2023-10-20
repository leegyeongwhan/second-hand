package com.secondhand.domain.login;


import com.secondhand.infrastructure.JwtProperties;
import com.secondhand.infrastructure.jwt.JwtTokenProvider;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.util.Date;

public class TokenCreator {

    private static final String secretKey = "2901ujr9021urf0u902hf021y90fh9c210hg093hg091h3g90h30gh901hg09h01";
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

