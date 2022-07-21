package com.bside.config.oauth.info

import com.bside.config.oauth.OAuth2UserInfo

class KakaoOAuth2UserInfo(override var attributes: Map<String, Any>): OAuth2UserInfo(attributes) {


    override val name: String?
        get() {
            val response = attributes["kakao_account"] as Map<String, Any>?
            val profile = response?.get("profile") as Map<String, Any>?
            return profile?.get("nickname") as String
        }
    override val email: String?
        get() {
            val response = attributes["kakao_account"] as Map<String, Any>?

            return response?.get("email") as String
        }
}