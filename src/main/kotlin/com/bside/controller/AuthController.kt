package com.bside.controller

import com.bside.dto.MemberRequestDto
import com.bside.dto.TokenDto
import com.bside.dto.TokenRequestDto
import com.bside.service.AuthService
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
    fun login(@RequestBody memberRequestDto: MemberRequestDto?): ResponseEntity<TokenDto?>? {
        return ResponseEntity.ok(authService.login(memberRequestDto!!))
    }

    @PostMapping("/reissue")
    fun reissue(@RequestBody tokenRequestDto: TokenRequestDto?): ResponseEntity<TokenDto?>? {
        return ResponseEntity.ok(authService.reissue(tokenRequestDto!!))
    }
}