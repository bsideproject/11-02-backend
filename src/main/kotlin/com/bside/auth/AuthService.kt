package com.bside.auth

import com.bside.member.entity.Member
import com.bside.config.jwt.TokenProvider
import com.bside.auth.repository.TokenRepository
import com.bside.common.type.Authority
import com.bside.common.type.ErrorMessage
import com.bside.common.type.ProviderType
import com.bside.member.dto.request.MemberRequestDto
import com.bside.auth.dto.request.TokenRequestDto
import com.bside.auth.dto.response.TokenResponseDto
import com.bside.auth.entity.RefreshToken
import com.bside.error.exception.AlreadyExistException
import com.bside.repository.MemberRepository

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
class AuthService(
        val memberRepository: MemberRepository,
        val refreshTokenRepository: TokenRepository,
        val passwordEncoder: PasswordEncoder,
        val tokenProvider: TokenProvider,
        val authenticationManagerBuilder: AuthenticationManagerBuilder
) {

    fun signup(memberRequestDto: MemberRequestDto) {
        if (memberRepository.existsByEmail(memberRequestDto.email)) {
            throw AlreadyExistException(ErrorMessage.USER_ALREADY_EXIST.name, ErrorMessage.USER_ALREADY_EXIST.reason)
        }

        memberRepository.save(
                Member(
                        email = memberRequestDto.email,
                        password = passwordEncoder.encode(memberRequestDto.password),
                        authority = Authority.ROLE_USER,
                        providerType = ProviderType.NAVER //FIXME 로컬 로그인 없애야 하기 때문에 임시 naver로 생성
                )
        )
    }

    fun login(memberRequestDto: MemberRequestDto): TokenResponseDto? {
        // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
        try {
            val authenticationToken: UsernamePasswordAuthenticationToken = memberRequestDto.toAuthentication()!!

            // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
            //    authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
            val authentication: Authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken)

            // 3. 인증 정보를 기반으로 JWT 토큰 생성

            val tokenDto: TokenResponseDto = tokenProvider.generateTokenDto(authentication, ProviderType.NAVER)

            // 4. RefreshToken 저장
            refreshTokenRepository.save(
                    RefreshToken(
                            key = authentication.name,
                            value = tokenDto.refreshToken!!
                    )
            )
            // 5. 토큰 발급
            return tokenDto
        } catch (e: Exception) {
            throw RuntimeException("internal server error")
        }
    }

    fun reissue(tokenRequestDto: TokenRequestDto): TokenResponseDto {
        // 1. Refresh Token 검증
        if (!tokenProvider.validateToken(tokenRequestDto.refreshToken)) {
            throw RuntimeException("Refresh Token 이 유효하지 않습니다.")
        }

        // 2. Access Token 에서 Member ID 가져오기
        val authentication: Authentication = tokenProvider.getAuthentication(tokenRequestDto.accessToken)

        // 3. 저장소에서 Member ID 를 기반으로 Refresh Token 값 가져옴
        val refreshToken: RefreshToken? = refreshTokenRepository.findByKey(authentication.name)

        if (refreshToken == null) {
            throw RuntimeException("로그아웃 된 사용자입니다.")
        }

        // 4. Refresh Token 일치하는지 검사
        if (refreshToken.value != tokenRequestDto.refreshToken) {
            throw RuntimeException("토큰의 유저 정보가 일치하지 않습니다.")
        }

        // 5. 새로운 토큰 생성
        val tokenDto: TokenResponseDto = tokenProvider.generateTokenDto(authentication, ProviderType.NAVER)

        refreshToken.value = tokenDto.refreshToken!!
        refreshTokenRepository.save(refreshToken)

        // 토큰 발급
        return tokenDto
    }
}