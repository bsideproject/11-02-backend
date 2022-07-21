package com.bside.config.oauth

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app")
class AppProperties {

    //private val oauth2: OAuth2 = OAuth2()

    class OAuth2 {
        private var authorizedRedirectUris: List<String> = ArrayList()

        fun authorizedRedirectUris(): List<String> {
            return authorizedRedirectUris
        }

        fun authorizedRedirectUris(authorizedRedirectUris: List<String>): OAuth2 {
            this.authorizedRedirectUris = authorizedRedirectUris
            return this
        }
    }
}