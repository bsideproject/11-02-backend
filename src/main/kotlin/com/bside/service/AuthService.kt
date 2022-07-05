package com.bside.service

import com.bside.dto.MemberRequestDto
import com.bside.dto.MemberResponseDto
import com.bside.dto.TokenDto
import com.bside.dto.TokenRequestDto


/**
 * name : AuthService
 * author : jisun.noh
 */
interface AuthService {
    fun signup( memberRequestDto: MemberRequestDto)

    fun login(memberRequestDto: MemberRequestDto): TokenDto

    fun reissue(tokenRequestDto: TokenRequestDto): TokenDto
}