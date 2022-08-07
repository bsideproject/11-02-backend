package com.bside.config.oauth

abstract class OAuth2UserInfo(open var attributes: Map<String, Any>) {
    abstract val name: String?
    abstract val email: String?
}