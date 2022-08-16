package com.bside.auth.dto.response

import com.bside.common.type.ProviderType


/**
 * name : TokenDto
 * author : jisun.noh
 */
class TokenResponseDto {
    var grantType: String? = null
    var accessToken: String? = null
    var refreshToken: String? = null
    var accessTokenExpiresIn: Long? = null
    var providerType: ProviderType? = null
}