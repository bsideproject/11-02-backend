package com.bside.controller

import com.bside.dto.MemberResponseDto
import com.bside.service.MemberService
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
    @GetMapping("email")
    fun getEmail(@RequestParam email: String): MemberResponseDto {
        return memberService.getMemberInfo(email)
    }

}