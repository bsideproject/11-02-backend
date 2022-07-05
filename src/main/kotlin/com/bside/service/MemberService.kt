package com.bside.service

import com.bside.dto.MemberResponseDto

/**
 * name : MemberService
 * author : jisun.noh
 */
interface MemberService {

    fun getMemberInfo(email: String): MemberResponseDto

}
