package com.bside.member.dto.request

import javax.validation.constraints.NotEmpty

data class MemberModifyRequest(
    @field:NotEmpty(message = "nickname is required")
    val nickname: String,
    var profile: String? = null
)