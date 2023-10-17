package com.secondhand.infrastructure.jwt;

import com.secondhand.domain.login.Token;
import com.secondhand.exception.v2.ErrorMessage;
import com.secondhand.exception.token.TokenException;
import com.secondhand.exception.token.TokenNotFoundException;
import com.secondhand.exception.token.TokenTimeException;
import com.secondhand.exception.v2.UnAuthorizedException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

@Slf4j
@Component
@EnableConfigurationProperties(JwtProperties.class)
@PropertySource("classpath:custom/setting.yml") // 설정정보 위치 생략가능
public class JwtTokenProvider {

    public static final String SUBJECT_NAME = "login_member";
    public static final int ACCESS_TOKEN_VALID_TIME = 24 * 60 * 60 * 1000;
    public static final long REFRESH_TOKEN_VALID_TIME = 30L * 24 * 60 * 60 * 1000L; // 30일 (long 형식 사용)
    public static final String MEMBER_ID = "memberId";
    public static final String TOKEN_TYPE = "tokenType";

    private final SecretKey secretKey;

    public JwtTokenProvider(JwtProperties jwtProperties) {
        this.secretKey = Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8));
    }


    /**
     * Access Token이 만료가 되면 서버는 만료되었다는 Response를 하게 된다.
     * 클라이언트는 해당 Response를 받으면 Refresh Token을 보낸다.
     * 서버는 Refresh Token 유효성 체크를 하게 되고, 새로운 Access Token을 발급한다. RefreshToken도 재발급한다.
     * 클라이언트는 새롭게 받은 Access Token을 기존의 Access Token에 덮어쓰게 된다.
     */

    public Token createToken(long memberId) {
        return Token.builder()
                .accessToken(createAccessToken(memberId))
                .refreshToken(createRefreshToken(memberId))
                .build();
    }

    public String createAccessToken(Long memberId) {
        Date now = new Date();
        Date accessTokenExpiration = new Date(now.getTime() + ACCESS_TOKEN_VALID_TIME);

        return Jwts.builder()
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .setIssuedAt(now)
                .setExpiration(accessTokenExpiration)
                .addClaims(Map.of(MEMBER_ID, memberId))
                .compact();
    }

    public String createRefreshToken(Long memberId) {
        Date now = new Date();
        Date refreshTokenExpiration = new Date(now.getTime() + REFRESH_TOKEN_VALID_TIME);

        return Jwts.builder()
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .setIssuedAt(now)
                .setExpiration(refreshTokenExpiration)
                .addClaims(Map.of(MEMBER_ID, memberId))
                .compact();
    }


    public void validateToken(final String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            throw new UnAuthorizedException(ErrorMessage.EXPIRED_TOKEN);
        } catch (JwtException e) {
            throw new UnAuthorizedException(ErrorMessage.INVALID_TOKEN);
        }
    }

    private void isValidateAccessToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(secretKey)
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
                    .setSigningKey(secretKey)
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
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return !claimsJws.getBody().isEmpty();
            // 여기서 추가적인 검증 로직을 넣을 수도 있습니다.
        } catch (RuntimeException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Long getSubject(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
        log.debug("claims = {}", claims);
        log.debug("MEMBER_ID = {}", claims.get(MEMBER_ID, Long.class));
        return claims.get(MEMBER_ID, Long.class);
    }

    public Long getSubjectRefreshSecretKey(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
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
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwt);
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

    public Map<String, Object> extractClaims(final String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return Collections.unmodifiableMap(claims);
    }

    public Long getExpiration(String accessToken) {
        Date expiration = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(accessToken)
                .getBody()
                .getExpiration();
        long now = new Date().getTime();
        return expiration.getTime() - now;
    }
}
