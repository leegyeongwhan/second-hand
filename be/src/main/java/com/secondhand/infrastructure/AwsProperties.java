package com.secondhand.infrastructure;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@ConfigurationProperties(prefix = "aws") // prefix 설정
public class AwsProperties {

    private final Credentials credentials;
    private final S3 s3;
    private final String region;

    @ConstructorBinding
    public AwsProperties(Credentials credentials, S3 s3, String region) {
        this.credentials = credentials;
        this.s3 = s3;
        this.region = region;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Credentials {

        private final String accessKey;
        private final String secretKey;
    }

    @Getter
    @RequiredArgsConstructor
    public static class S3 {

        private final String bucket;
    }
}

