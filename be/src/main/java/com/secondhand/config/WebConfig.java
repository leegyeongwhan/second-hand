package com.secondhand.config;

import com.secondhand.presentation.interceptor.LoginInterceptor;
import com.secondhand.presentation.suport.LoginArgumentResolver;
import com.secondhand.presentation.suport.NotNullParamArgumentResolver;
import com.secondhand.presentation.suport.converter.OAuthProviderConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import reactor.netty.http.client.HttpClient;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final LoginInterceptor loginInterceptor;
    private final OAuthProviderConverter oAuthProviderConverter;
    private final NotNullParamArgumentResolver notNullParamArgumentResolver;
    private final LoginArgumentResolver loginArgumentResolver;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedHeaders("*")
                .allowedMethods("HEAD", "OPTIONS", "GET", "POST", "PUT", "DELETE", "PATCH");
    }

    @Bean
    public HttpClient httpClient() {
        return HttpClient.create();
    }

    @Bean
    public WebClient webClient(HttpClient httpClient) {
        return WebClient.builder().clientConnector(new ReactorClientHttpConnector(httpClient)).build();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/api/products");
    }
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(oAuthProviderConverter);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginArgumentResolver);
        resolvers.add(notNullParamArgumentResolver);
    }
}
