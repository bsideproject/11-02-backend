package com.bside.config.oauth.handler

import com.bside.common.util.CookieUtil
import com.bside.config.oauth.repository.OAuth2AuthorizationRequestBasedOnCookieRepository
import com.bside.config.oauth.repository.OAuth2AuthorizationRequestBasedOnCookieRepository.Companion.REDIRECT_URI_PARAM_COOKIE_NAME
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler
import org.springframework.web.util.UriComponentsBuilder
import java.io.IOException
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class OAuth2AuthenticationFailureHandler(
    val authorizationRequestRepository: OAuth2AuthorizationRequestBasedOnCookieRepository
): SimpleUrlAuthenticationFailureHandler() {

    @Throws(IOException::class, ServletException::class)
    override fun onAuthenticationFailure(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        exception: AuthenticationException
    ) {
        var targetUrl: String = CookieUtil.getCookie(request!!, REDIRECT_URI_PARAM_COOKIE_NAME)!!.value as String ?: "/"

        exception.printStackTrace()
        targetUrl = UriComponentsBuilder.fromUriString(targetUrl)
            .queryParam("error", exception.localizedMessage)
            .build().toUriString()
        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response)
        redirectStrategy.sendRedirect(request, response, targetUrl)
    }
}