package com.bside.config.jwt

import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * name : JwtAccessDeniedHandler
 * author : jisun.noh
 */
@Component
class JwtAccessDeniedHandler : AccessDeniedHandler {
    override fun handle(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        accessDeniedException: AccessDeniedException?
    ) {
        // 필요한 권한이 없이 접근하려 할 때 403
        response?.sendError(HttpServletResponse.SC_FORBIDDEN)
    }
}