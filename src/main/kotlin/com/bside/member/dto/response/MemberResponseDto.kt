package com.bside.member.dto.response

import com.bside.member.entity.Member
import com.bside.member.type.Gender


data class MemberResponseDto (
    val email: String,
    val name: String,
    val nickname: String?,
    val birthday: String?,
    val gender: Gender?,
    var profile: String? = null,
    var score: Int? = 0
) {
    companion object {
        fun fromEntity(member: Member) = MemberResponseDto(
            member.email,
            member.name,
            member.nickname,
            member.birthday,
            member.gender,
            member.profile,
            member.score
        )
    }
}