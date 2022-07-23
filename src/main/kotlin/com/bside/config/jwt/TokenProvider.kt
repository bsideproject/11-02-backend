package com.bside.config.jwt


import com.bside.common.type.ProviderType
import com.bside.dto.response.TokenResponseDto
import com.bside.dto.TokenDto
import com.bside.util.logger


import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys

import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component

import java.util.*
import java.util.stream.Collectors

/**
 * name : TokenProvider
 * author : jisun.noh
 */
@Component
class TokenProvider(@Value("\${jwt.secret}") secretKey: String) {

    private val logger by logger()

    companion object {
        val AUTHORITIES_KEY = "auth"
        val BEARER_TYPE = "bearer"
        val ACCESS_TOKEM_EXPIRE_TIME = 1000 * 60 * 10 // 30 min
        val REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7 // 7day
    }

    //Secret 값은 특정 문자열을 Base64 로 인코딩한 값 사용
    private val key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey))

    fun generateTokenDto(authentication: Authentication, providerType: ProviderType): TokenResponseDto {
        //권한들 가져오기
        val authorities = authentication.authorities.stream()
            .map { obj: GrantedAuthority -> obj.authority }
            .collect(Collectors.joining(","))

        val now = Date().time

        //Access Token 생성
        val accessTokenExpiredsIn = Date(now + ACCESS_TOKEM_EXPIRE_TIME)
        val accessToken = Jwts.builder()
            .setSubject(authentication.name) //payload "sub":"name"
            .claim(AUTHORITIES_KEY, authorities) //payload "auth":"ROLE_USER"
            .setExpiration(accessTokenExpiredsIn)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()

        //Refresh Token 생성
        val refreshToken = Jwts.builder()
            .setExpiration(Date(now + REFRESH_TOKEN_EXPIRE_TIME))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()

        return TokenResponseDto().apply {
            this.grantType = BEARER_TYPE
            this.accessToken = accessToken
            this.accessTokenExpiresIn = accessTokenExpiredsIn.time
            this.refreshToken = refreshToken
            this.providerType = providerType
        }
    }

    /**
     * description : 토큰 복호화
     */
    fun getAuthentication(accessToken: String): Authentication {
        // 토큰 복호화
        val claims: Claims = parseClaims(accessToken)

        if (claims.get(AUTHORITIES_KEY) == null) {
            throw RuntimeException("권한 정보가 없는 토큰입니다.")
        }

        // 클레임에서 권한 정보 가져오기
        val authorities: Collection<GrantedAuthority?> =
            Arrays.stream(claims[AUTHORITIES_KEY].toString().split(",").toTypedArray())
                .map { role: String? -> SimpleGrantedAuthority(role) }
                .collect(Collectors.toList())

        val principal: UserDetails = User(claims.subject, "", authorities)

        return UsernamePasswordAuthenticationToken(principal, "", authorities)
    }

    fun validateToken(token: String): Boolean {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
            return true
        } catch (e: SecurityException) {
            logger.info("잘못된 JWT 서명입니다.")
        } catch (e: ExpiredJwtException) {
            logger.info("만료된 JWT 토큰입니다.")
        } catch (e: UnsupportedJwtException) {
            logger.info("지원되지 않는 JWT 토큰입니다.")
        } catch (e: IllegalArgumentException) {
            logger.info("JWT 토큰이 잘못되었습니다.")
        }
        return false
    }

    private fun parseClaims(accessToken: String): Claims {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).body
        } catch (e: ExpiredJwtException) {
            return e.claims
        }
    }

}