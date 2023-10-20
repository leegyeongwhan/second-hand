package com.secondhand.infrastructure;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@ConfigurationProperties(prefix = "aws.jwt") // prefix 설정
public class JwtProperties {
    private final String secretKey;

    @ConstructorBinding
    public JwtProperties(String secretKey) {
        this.secretKey = secretKey;
    }
}
