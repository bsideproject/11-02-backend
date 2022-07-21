package com.bside.repository

import com.bside.common.CookieUtil
import com.nimbusds.oauth2.sdk.util.StringUtils.isNotBlank
import org.apache.logging.log4j.util.Strings.isNotBlank
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest
import org.springframework.util.StringUtils
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class OAuth2AuthorizationRequestBasedOnCookieRepository: AuthorizationRequestRepository<OAuth2AuthorizationRequest> {
    companion object {
        val OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request"
        val REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri"
        val REFRESH_TOKEN = "refresh_token"
    }
    private val cookieExpireSeconds = 180

    override fun loadAuthorizationRequest(request: HttpServletRequest?): OAuth2AuthorizationRequest {
        val cookie = CookieUtil.getCookie(request!!, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
        return CookieUtil.deserialize(cookie!!, OAuth2AuthorizationRequest::class.java)
    }

    override fun saveAuthorizationRequest(
        authorizationRequest: OAuth2AuthorizationRequest?,
        request: HttpServletRequest?,
        response: HttpServletResponse?
    ) {
        if (authorizationRequest == null) {
            CookieUtil.deleteCookie(request!!, response!!, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
            CookieUtil.deleteCookie(request!!, response!!, REDIRECT_URI_PARAM_COOKIE_NAME)
            CookieUtil.deleteCookie(request!!, response!!, REFRESH_TOKEN)
            return
        }

        CookieUtil.addCookie(
            response!!,
            OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME,
            CookieUtil.serialize(authorizationRequest),
            cookieExpireSeconds
        )
        val redirectUriAfterLogin = request!!.getParameter(REDIRECT_URI_PARAM_COOKIE_NAME)
        if (redirectUriAfterLogin.isNotBlank()) {
            CookieUtil.addCookie(response!!, REDIRECT_URI_PARAM_COOKIE_NAME, redirectUriAfterLogin, cookieExpireSeconds)
        }
    }

    override fun removeAuthorizationRequest(request: HttpServletRequest?): OAuth2AuthorizationRequest {
        return this.loadAuthorizationRequest(request);
    }

    fun removeAuthorizationRequestCookies(request: HttpServletRequest?, response: HttpServletResponse?) {
        CookieUtil.deleteCookie(request!!, response!!, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
        CookieUtil.deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME)
        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN)
    }

}