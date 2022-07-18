package com.bside.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.*
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.service.contexts.SecurityContext
import springfox.documentation.spring.web.plugins.Docket
import java.util.*


@Profile("local || dev")
@Configuration
class SwaggerConfig {

    @Bean
    fun api(): Docket? {
        //Docket: Swagger 설정의 핵심이 되는 Bean
        return Docket(DocumentationType.OAS_30)
            .securityContexts(listOf(securityContext()))
            .securitySchemes(listOf(apiKey()) as List<SecurityScheme>?)
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.bside.controller")) // controller package 지정
            .paths(PathSelectors.any())
            .build()
            .apiInfo(apiInfo()) //:Swagger UI 로 노출할 정보
    }

    // api 정보 등록
    private fun apiInfo(): ApiInfo? {
        return ApiInfoBuilder()
            .title("Bside 11 Swagger")
            .description("Bside 11 backend Swagger")
            .version("1.0")
            .build()
    }

    //인증하는 방식 설정
    private fun securityContext(): SecurityContext? {
        return SecurityContext
            .builder()
            .securityReferences(defaultAuth()).forPaths(PathSelectors.any()).build();
    }

    private fun defaultAuth(): List<SecurityReference?>? {
        val authorizationScope = AuthorizationScope("global", "accessEverything")
        val authorizationScopes = arrayOfNulls<AuthorizationScope>(1)
        authorizationScopes[0] = authorizationScope
        return Arrays.asList(SecurityReference("Authorization", authorizationScopes))
    }

    //api key 설정
    private fun apiKey(): ApiKey? {
        return ApiKey("Authorization", "Authorization", "header")
    }

}