package com.bside.config.oauth.handler

import com.bside.common.CookieUtil
import com.bside.common.type.Authority
import com.bside.common.type.ProviderType
import com.bside.config.jwt.TokenProvider
import com.bside.config.oauth.AppProperties
import com.bside.config.oauth.OAuth2UserInfo
import com.bside.config.oauth.info.Oauth2UserInfoFactory
import com.bside.dto.response.TokenDto
import com.bside.entity.RefreshToken
import com.bside.repository.OAuth2AuthorizationRequestBasedOnCookieRepository
import com.bside.repository.OAuth2AuthorizationRequestBasedOnCookieRepository.Companion.REDIRECT_URI_PARAM_COOKIE_NAME
import com.bside.repository.OAuth2AuthorizationRequestBasedOnCookieRepository.Companion.REFRESH_TOKEN
import com.bside.repository.TokenRepository
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class OAuth2AuthenticationSuccessHandler(
    val tokenProvider: TokenProvider,
    val tokenRepository: TokenRepository,
    val appProperties: AppProperties,
    val authorizationRequestRepository: OAuth2AuthorizationRequestBasedOnCookieRepository,
) : SimpleUrlAuthenticationSuccessHandler() {
    override fun onAuthenticationSuccess(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authentication: Authentication?
    ) {
        val targetUrl: String = determineTargetUrl(request, response, authentication!!);

        if (response!!.isCommitted) {
            logger.debug("Response has already been committed. Unable to redirect to ${targetUrl}")
            return
        }
        clearAuthenticationAttributes(request, response)

        redirectStrategy.sendRedirect(request, response, targetUrl);
    }

    override fun determineTargetUrl(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authentication: Authentication
    ): String {
        val redirectUri: String = CookieUtil.getCookie(request!!, REDIRECT_URI_PARAM_COOKIE_NAME)!!.value

        if (redirectUri.isEmpty() && !isAuthorizedRedirectUri(redirectUri)) {
            throw IllegalArgumentException("Sorry! We've got an Unauthorized Redirect URI and can't proceed with the authentication");
        }

        val targetUrl: String = redirectUri ?: defaultTargetUrl

        val authToken = authentication as OAuth2AuthenticationToken
        val providerType: ProviderType =
            ProviderType.valueOf(authToken.authorizedClientRegistrationId.uppercase(Locale.getDefault()))

        val tokenDto: TokenDto = tokenProvider.generateTokenDto(authentication, providerType)
        val refreshToken: RefreshToken = RefreshToken(
            key = authentication.name,
            value = tokenDto.refreshToken!!
        )
        // 4. RefreshToken 저장
        tokenRepository.save(refreshToken)

        val cookieMaxAge: Int = TokenProvider.REFRESH_TOKEN_EXPIRE_TIME

        CookieUtil.deleteCookie(request, response!!, REFRESH_TOKEN)
        CookieUtil.addCookie(response!!, REFRESH_TOKEN, refreshToken.value, cookieMaxAge)

        return UriComponentsBuilder.fromUriString(targetUrl)
            .queryParam("token", tokenDto.accessToken)
            .build().toUriString()
    }

    protected fun clearAuthenticationAttributes(request: HttpServletRequest?, response: HttpServletResponse?) {
        super.clearAuthenticationAttributes(request)
        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response)
    }

    private fun hasAuthority(
        authorities: Collection<GrantedAuthority>?,
        authority: String
    ): Boolean {
        if (authorities == null) {
            return false
        }
        for (grantedAuthority in authorities) {
            if (authority == grantedAuthority.authority) {
                return true
            }
        }
        return false
    }

    private fun isAuthorizedRedirectUri(uri: String): Boolean {
        val clientRedirectUri: URI = URI.create(uri)
        AppProperties.OAuth2().authorizedRedirectUris().forEach {
            val authorizedURI: URI = URI.create(it)
            if (authorizedURI.host.equals(clientRedirectUri.host)
                && authorizedURI.port === clientRedirectUri.port
            ) {
                return true
            }
            return false
        }
        return false
    }

}