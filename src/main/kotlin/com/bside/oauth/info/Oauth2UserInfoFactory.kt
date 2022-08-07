package com.bside.oauth.info

import com.bside.common.type.ProviderType
import com.bside.config.oauth.OAuth2UserInfo


class Oauth2UserInfoFactory {
    companion object {
        fun getOAuth2UserInfo(providerType: ProviderType, attributes: Map<String, Any>): OAuth2UserInfo {
            return when (providerType) {
                ProviderType.GOOGLE -> GoogleOAuth2UserInfo(attributes)
                ProviderType.NAVER -> NaverOAuth2UserInfo(attributes)
                ProviderType.KAKAO -> KakaoOAuth2UserInfo(attributes)
            }
        }
    }
}