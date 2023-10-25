package com.secondhand.infrastructure;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@ConfigurationProperties(prefix = "redis")
public class RedisProperties {

    private final int port;
    private final String host;

    @ConstructorBinding
    public RedisProperties(int port, String host) {
        this.port = port;
        this.host = host;
    }
}
