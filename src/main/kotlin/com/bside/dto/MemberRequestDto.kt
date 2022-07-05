package com.bside.dto

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken

/**
 * name : MemberRequestDto
 * author : jisun.noh
 */
class MemberRequestDto {
    var email: String = ""
    var password: String = ""

    fun toAuthentication(): UsernamePasswordAuthenticationToken? {
        return UsernamePasswordAuthenticationToken(email, password)
    }
}
