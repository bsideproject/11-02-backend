package com.bside.service.impl

import com.bside.dto.*
import com.bside.entity.Member
import com.bside.config.jwt.TokenProvider
import com.bside.repository.MemberReposiroty
import com.bside.repository.TokenRepository
import com.bside.service.AuthService
import com.bside.entity.type.Authority
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.lang.RuntimeException

/**
 * name : AuthServiceImpl
 * author : jisun.noh
 */
@Service
class AuthServiceImpl(
    val memberRepository: MemberReposiroty,
    val refreshTokenRepository: TokenRepository,
    val passwordEncoder: PasswordEncoder,
    val tokenProvider: TokenProvider,
    val authenticationManagerBuilder: AuthenticationManagerBuilder
) : AuthService {

    override fun signup(memberRequestDto: MemberRequestDto) {
        if (memberRepository.existsByEmail(memberRequestDto.email)) {
            throw RuntimeException("이미 가입되어 있는 유저입니다")
        }

        val member: Member = Member().apply {
            this.email = memberRequestDto.email
            this.password = passwordEncoder.encode(memberRequestDto.password)
            this.authority = Authority.ROLE_USER
        }

        memberRepository.save(member)
    }

    override fun login(memberRequestDto: MemberRequestDto): TokenDto {
        // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
        val authenticationToken: UsernamePasswordAuthenticationToken = memberRequestDto.toAuthentication()!!

        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        //    authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        val authentication: Authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken)

        // 3. 인증 정보를 기반으로 JWT 토큰 생성

        val tokenDto: TokenDto = tokenProvider.generateTokenDto(authentication)

        // 4. RefreshToken 저장
        val refreshToken: RefreshToken = RefreshToken().apply {
            this.key = authentication.name
            this.value = tokenDto.refreshToken!!
        }
        refreshTokenRepository.save(refreshToken)

        // 5. 토큰 발급
        return tokenDto
    }

    override fun reissue(tokenRequestDto: TokenRequestDto): TokenDto {
        // 1. Refresh Token 검증
        if (!tokenProvider.validateToken(tokenRequestDto.refreshToken)) {
            throw RuntimeException("Refresh Token 이 유효하지 않습니다.")
        }

        // 2. Access Token 에서 Member ID 가져오기
        val authentication: Authentication = tokenProvider.getAuthentication(tokenRequestDto.accessToken)

        // 3. 저장소에서 Member ID 를 기반으로 Refresh Token 값 가져옴
        val refreshToken: RefreshToken = refreshTokenRepository.findByKey(authentication.name)

        if (refreshToken == null) {
            throw RuntimeException("로그아웃 된 사용자입니다.")
        }

        // 4. Refresh Token 일치하는지 검사
        if (refreshToken.value != tokenRequestDto.refreshToken) {
            throw RuntimeException("토큰의 유저 정보가 일치하지 않습니다.")
        }

        // 5. 새로운 토큰 생성
        val tokenDto: TokenDto = tokenProvider.generateTokenDto(authentication)

        // 6. 저장소 정보 업데이트
        val newRefreshToken: RefreshToken = RefreshToken().apply {
            this.value = tokenDto.refreshToken!!
        }
        refreshTokenRepository.save(newRefreshToken)

        // 토큰 발급
        return tokenDto
    }
}