package com.bside.auth

import com.bside.member.dto.request.MemberRequestDto
import com.bside.auth.dto.response.TokenResponseDto
import com.bside.auth.dto.request.TokenRequestDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * name : AuthController
 * author : jisun.noh
 */
@RestController
@RequestMapping("/auth")
class AuthController(val authService: AuthService) {

    @PostMapping("/signup")
    fun signup(@RequestBody memberRequestDto: MemberRequestDto?): ResponseEntity<Any>? {
        authService.signup(memberRequestDto!!)
        return ResponseEntity.ok("ok")
    }

    @PostMapping("/login")
    fun login(@RequestBody memberRequestDto: MemberRequestDto?): ResponseEntity<TokenResponseDto?>? {
        return ResponseEntity.ok(authService.login(memberRequestDto!!))
    }

    @PostMapping("/reissue")
    fun reissue(@RequestBody tokenRequestDto: TokenRequestDto?): ResponseEntity<TokenResponseDto?>? {
        return ResponseEntity.ok(authService.reissue(tokenRequestDto!!))
    }
}