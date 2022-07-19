package com.bside.config.jwt

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration


@Configuration
@ConfigurationProperties(prefix = "spring.security.jwt")
class JwtProperties {
    lateinit var secret: String
}