package com.secondhand.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.secondhand.infrastructure.jwt.JwtTokenProvider;
import com.secondhand.presentation.filter.AuthExceptionHandlerFilter;
import com.secondhand.presentation.filter.JwtFilter;
import com.secondhand.presentation.suport.AuthenticationContext;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class FilterConfig {

    private final JwtTokenProvider jwtProvider;
    private final AuthenticationContext authenticationContext;
    private final ObjectMapper objectMapper;

    // AuthExceptionHandlerFilter필터를 거쳐 예외가있는지확인한후
    // jwt필터를 거친다
    @Bean
    public FilterRegistrationBean<AuthExceptionHandlerFilter> authExceptionHandlerFilter() {
        FilterRegistrationBean<AuthExceptionHandlerFilter> authExceptionHandlerFilter = new FilterRegistrationBean<>();
        authExceptionHandlerFilter.setFilter(new AuthExceptionHandlerFilter(objectMapper));
        authExceptionHandlerFilter.addUrlPatterns("/api/*");
        authExceptionHandlerFilter.setOrder(1);
        return authExceptionHandlerFilter;
    }

    @Bean
    public FilterRegistrationBean<JwtFilter> jwtFilter() {
        FilterRegistrationBean<JwtFilter> jwtFilter = new FilterRegistrationBean<>();
        jwtFilter.setFilter(new JwtFilter(jwtProvider, authenticationContext));
        jwtFilter.addUrlPatterns("/api/*");
        jwtFilter.setOrder(2);
        return jwtFilter;
    }
}

