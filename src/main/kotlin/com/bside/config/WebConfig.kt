package com.bside.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig: WebMvcConfigurer {

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .exposedHeaders("X-AUTH_TOKEN")
            .allowCredentials(true)
            .allowedOriginPatterns("localhost:3000","localhost:8080", "175.45.194.10","175.106.97.109")
    }
}