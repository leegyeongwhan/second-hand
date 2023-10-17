package com.secondhand.config;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;

@ConfigurationPropertiesScan("com.secondhand.infrastructure")
@Configuration
public class PropertiesConfig {
}
