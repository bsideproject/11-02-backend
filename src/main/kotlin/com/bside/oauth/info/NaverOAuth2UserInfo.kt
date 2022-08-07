package com.bside.oauth.info

import com.bside.config.oauth.OAuth2UserInfo

class NaverOAuth2UserInfo(override var attributes: Map<String, Any>): OAuth2UserInfo(attributes) {

    override val name: String?
        get() {
            val response = attributes["response"] as Map<String, Any>?

            return response?.get("nickname") as String
        }
    override val email: String?
        get() {
            val response = attributes["response"] as Map<String, Any>?

            return response?.get("email") as String
        }
}