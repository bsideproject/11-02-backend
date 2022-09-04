package com.bside.member.dto.response

import com.bside.common.type.Authority
import com.bside.member.entity.Member
import com.bside.member.type.Gender

/**
 * name : MemberResponseDto
 * author : jisun.noh
 */
data class MemberResponse(
        val id: String = "",
        val email: String = "",
        val password: String = "",
        val name: String = "",
        val nickname: String? = "",
        val authority: Authority = Authority.ROLE_USER,
        val profile: String? = "",
        val gender: Gender? = null,
        val birthday: String? = null,
        val isLeader: Boolean = false
) {

    companion object {
        fun fromEntity(member: Member) = MemberResponse(id = member.id.toString(), name = member.name, nickname = member.nickname, profile = member.profile)
    }

}