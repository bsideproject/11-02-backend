package com.bside.member.dto.response

import com.bside.member.entity.Member
import com.bside.member.type.Gender

data class MemberModifyResponse(
    val email: String,
    val name: String,
    val nickname: String,
    val birthday: String,
    val gender: Gender?,
    var profile: String? = null
) {

    constructor(member: Member): this(
        member.email,
        member.name,
        member.nickname ?: "",
        member.birthday ?: "",
        member.gender,
        member.profile
    )
}