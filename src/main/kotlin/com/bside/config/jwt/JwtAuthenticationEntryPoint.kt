package com.bside.config.jwt

import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * name : JwtAuthenticationEntryPoint
 * author : jisun.noh
 */
@Component
class JwtAuthenticationEntryPoint : AuthenticationEntryPoint {
    override fun commence(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authException: AuthenticationException?
    ) {
        // 유효한 자격증명을 제공하지 않고 접근하려 할 때 401
        response?.sendError(HttpServletResponse.SC_UNAUTHORIZED)
    }
}