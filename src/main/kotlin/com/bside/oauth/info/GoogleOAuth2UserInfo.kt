package com.bside.oauth.info

import com.bside.config.oauth.OAuth2UserInfo

class GoogleOAuth2UserInfo(override var attributes: Map<String, Any>): OAuth2UserInfo(attributes) {

    override val name: String?
        get() = attributes["name"] as String
    override val email: String?
        get() = attributes["email"] as String
}