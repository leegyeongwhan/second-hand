package com.secondhand.infrastructure.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Slf4j
@Component
public class AuthorizationExtractor {
    public static final String BEARER = "bearer";

    public static Optional<String> extract(HttpServletRequest request) {
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (!StringUtils.hasText(header) || !header.toLowerCase().startsWith(BEARER)) {
            return Optional.empty();
        }

        return Optional.of(header.split(" ")[1]);
    }

    private AuthorizationExtractor() {}
}
