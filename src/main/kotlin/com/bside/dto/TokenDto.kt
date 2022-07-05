package com.bside.dto


/**
 * name : TokenDto
 * author : jisun.noh
 */
class TokenDto {
    var grantType: String? = null
    var accessToken: String? = null
    var refreshToken: String? = null
    var accessTokenExpiresIn: Long? = null
}