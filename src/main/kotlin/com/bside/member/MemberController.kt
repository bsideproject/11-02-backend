package com.bside.member

import com.bside.member.dto.response.MemberResponseDto
import com.bside.util.logger

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


/**
 * name : MemberController
 * author : jisun.noh
 */
@RestController
@RequestMapping("/")
class MemberController(
        val memberService: MemberService
) {
    private val logger by logger()

    @GetMapping("email")
    fun getEmail(@RequestParam email: String): MemberResponseDto {
        return memberService.getMemberInfo(email)
    }
}