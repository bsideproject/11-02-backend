package com.bside.config.filter

import com.bside.common.util.CookieUtil
import com.bside.config.jwt.TokenProvider

import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter

import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


/**
 * name : JwtFilter
 * author : jisun.noh
 */
class JwtFilter(val tokenProvider: TokenProvider) : OncePerRequestFilter() {

    val AUTHORIZATION_HEADER = "Authorization"
    val BEARER_PREFIX = "Bearer"

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        // 1. Request Header에서 토큰 get
        //val jwt: String? = resolveToken(request) TODO 추후 header로 변경 예정
        val jwt: String? = CookieUtil.getCookie(request, "access_token")?.value

        // 2. validateToken
        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt!!)) {
            val authentication: Authentication = tokenProvider.getAuthentication(jwt!!)
            SecurityContextHolder.getContext().authentication = authentication
        }
        filterChain.doFilter(request, response)
    }

    private fun resolveToken(request: HttpServletRequest): String? {
        val bearerToken = if (request.getHeader(AUTHORIZATION_HEADER) != null) {
            request.getHeader(AUTHORIZATION_HEADER)
        } else {
            return null
        }

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7)
        }
        return null
    }
}