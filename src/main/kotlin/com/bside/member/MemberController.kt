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

    // 슬랙 에러알람 확인을 위한 테스트 용도 입니다. 다음 개발시 삭제 예정입니다.
    @GetMapping("/error")
    fun makeError() {
        try {
            val v = 1 / 0
        } catch (e: Exception) {
            logger.error("error occurred", e)
        }
    }

}