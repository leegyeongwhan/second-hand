package com.secondhand.domain.login;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.secondhand.domain.member.Member;
import com.secondhand.exception.token.TokenException;
import com.secondhand.exception.token.TokenNotFoundException;
import com.secondhand.exception.token.TokenTimeException;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
public class JwtTokenProvider {

    public static final String SUBJECT_NAME = "login_member";
    public static final String MEMBER_ID = "memberId";
    public static final String TOKEN_TYPE = "tokenType";
    public static final int ACCESS_TOKEN_VALID_TIME = 24 * 60 * 60 * 1000;
    public static final long REFRESH_TOKEN_VALID_TIME = 30L * 24 * 60 * 60 * 1000L; // 30일 (long 형식 사용)
    private final String secret;
    private final String refreshSecretKey;

    public JwtTokenProvider(@Value("${JWT_SECRET_KEY}") String secret,
                            @Value("${JWT_SECRET_REFRESH_KEY}") String refreshSecretKey) {
        this.secret = secret;
        this.refreshSecretKey = refreshSecretKey;
    }

    /**
     * Access Token이 만료가 되면 서버는 만료되었다는 Response를 하게 된다.
     * 클라이언트는 해당 Response를 받으면 Refresh Token을 보낸다.
     * 서버는 Refresh Token 유효성 체크를 하게 되고, 새로운 Access Token을 발급한다. RefreshToken도 재발급한다.
     * 클라이언트는 새롭게 받은 Access Token을 기존의 Access Token에 덮어쓰게 된다.
     */

    public Token createToken(Member member) throws JsonProcessingException {

        String accessToken = Jwts.builder()
                .setSubject(SUBJECT_NAME)
                .claim(MEMBER_ID,  member.getId()) //페이로드,헤더는 자동설정
                .setExpiration(new Date((new Date()).getTime() + ACCESS_TOKEN_VALID_TIME)) // 토큰의 만료일을 설정 : 현재 10일
                .signWith(SignatureAlgorithm.HS256, secret) // HS256 알고리즘과 시크릿 키를 사용하여 서명
                .compact();

        String refreshToken = Jwts.builder()
                .setSubject(SUBJECT_NAME)
                .claim(MEMBER_ID,  member.getId()) //페이로드,헤더는 자동설정
                .setIssuedAt(new Date()) // 토큰 발행 시간 정보
                .setExpiration(new Date((new Date()).getTime() + REFRESH_TOKEN_VALID_TIME)) // 토큰의 만료일을 설정 : 현재 10일
                .signWith(SignatureAlgorithm.HS256, refreshSecretKey)  // 사용할 암호화 알고리즘과
                // signature 에 들어갈 secret값 세팅
                .compact();

        return Token.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }


    public TokenType validateToken(String token) {
        if (isAccessToken(token)) {
            isValidateAccessToken(token);
            return TokenType.ACCESS_TOKEN;
        } else if (isRefreshToken(token)) {
            return TokenType.REFRESH_TOKEN;
        } else {
            throw new TokenException();
        }
    }

    private void isValidateAccessToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token);
            if (!claimsJws.getBody().getExpiration().before(new Date())) {
                claimsJws.getBody();
            }

        } catch (ExpiredJwtException | SignatureException e) {
            throw new TokenTimeException();
        } catch (MalformedJwtException e) {
            throw new TokenNotFoundException();
        } catch (RuntimeException e) {
            throw new TokenException();
        }
    }

    public boolean isRefreshToken(String token) throws RuntimeException {
        try {
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(refreshSecretKey)
                    .parseClaimsJws(token);

            return !claimsJws.getBody().getExpiration().before(new Date()) && !claimsJws.getBody().isEmpty();

        } catch (ExpiredJwtException | SignatureException e) {
            throw new TokenTimeException();
        } catch (MalformedJwtException e) {
            throw new TokenNotFoundException();
        } catch (RuntimeException e) {
            throw new TokenException();
        }
    }

    private boolean isAccessToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return !claimsJws.getBody().isEmpty();
            // 여기서 추가적인 검증 로직을 넣을 수도 있습니다.
        } catch (RuntimeException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Long getSubject(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
        log.debug("claims = {}", claims);
        log.debug("MEMBER_ID = {}", claims.get(MEMBER_ID, Long.class));
        return claims.get(MEMBER_ID, Long.class);
    }

    public Long getSubjectRefreshSecretKey(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(refreshSecretKey)
                .parseClaimsJws(token)
                .getBody();
        log.debug("claims = {}", claims);
        log.debug("MEMBER_ID = {}", claims.get(MEMBER_ID, Long.class));
        return claims.get(MEMBER_ID, Long.class);
    }

    public String getUserNameFromJwt(String jwt) {
        return getClaims(jwt).getBody().getId();
    }

    private Jws<Claims> getClaims(String jwt) {
        try {
            return Jwts.parser().setSigningKey(secret).parseClaimsJws(jwt);
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature");
            throw new JwtException("토큰이 유효하지 않습니다");
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
            throw new JwtException("토큰이 유효하지 않습니다");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
            throw new JwtException("토큰이 만료되었습니다.");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
            throw new JwtException("지원하지 않는 토큰 타입입니다");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
            throw new JwtException("빈 토큰입니다");
        }
    }
}
