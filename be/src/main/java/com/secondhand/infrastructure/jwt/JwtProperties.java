package com.secondhand.infrastructure.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@RequiredArgsConstructor // 롬복 생성자
@ConstructorBinding
@ConfigurationProperties(prefix = "jwt") // prefix 설정
public class JwtProperties {

    private final String secretKey;
}
