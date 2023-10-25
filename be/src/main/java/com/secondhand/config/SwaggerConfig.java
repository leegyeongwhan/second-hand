//package com.secondhand.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.service.ApiKey;
//import springfox.documentation.service.AuthorizationScope;
//import springfox.documentation.service.SecurityReference;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spi.service.contexts.SecurityContext;
//import springfox.documentation.spring.web.plugins.Docket;
//
//import java.util.Arrays;
//import java.util.List;
//
//@Configuration
//public class SwaggerConfig {
//    private String version = "V0.1";
//
//    @Bean
//    public Docket api() {
//        return new Docket(DocumentationType.OAS_30)
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.secondhand.web.controller"))
//                .paths(PathSelectors.any())
//                .build()
//                .apiInfo(apiInfo())
//                .securityContexts(Arrays.asList(securityContext())) // *
//                .securitySchemes(Arrays.asList(apiKey())); // **
//    }
//
//    private ApiInfo apiInfo() {
//        return new ApiInfoBuilder()
//                .title("second_hand")
//                .description("second_hand api 명세서")
//                .version(version)
//                .build();
//    }
//
//    private ApiKey apiKey() {
//        return new ApiKey("JWT", "Authorization", "header");
//    }
//
//    //JWT SecurityContext 구성
//    private SecurityContext securityContext() {
//        return SecurityContext.builder().securityReferences(defaultAuth()).build();
//    }
//
//    private List<SecurityReference> defaultAuth() {
//        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEveryThing");
//        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
//        authorizationScopes[0] = authorizationScope;
//        return Arrays.asList(new SecurityReference("JWT", authorizationScopes));
//    }
//}
