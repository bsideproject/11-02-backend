package com.bside.oauth

import com.bside.common.type.ProviderType
import com.bside.config.oauth.OAuth2UserInfo
import com.bside.oauth.exception.OAuthProviderMissMatchException
import com.bside.oauth.info.Oauth2UserInfoFactory.Companion.getOAuth2UserInfo
import com.bside.member.entity.Member
import com.bside.oauth.info.UserPrincipal
import com.bside.member.repository.MemberRepository
import org.springframework.security.authentication.InternalAuthenticationServiceException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*


@Service
class CustomOAuth2UserService(
    val memberRepository: MemberRepository
): DefaultOAuth2UserService() {

    override fun loadUser(userRequest: OAuth2UserRequest?): OAuth2User? {
        val user = super.loadUser(userRequest)
        return try {
            this.process(userRequest!!, user)
        } catch (ex: AuthenticationException) {
            throw ex
        } catch (ex: Exception) {
            ex.printStackTrace()
            throw InternalAuthenticationServiceException(ex.message, ex.cause)
        }
    }

    private fun process(userRequest: OAuth2UserRequest, user: OAuth2User): OAuth2User? {
        val providerType: ProviderType = ProviderType.valueOf(
            userRequest.clientRegistration.registrationId.uppercase(
                Locale.getDefault()
            )
        )
        val userInfo: OAuth2UserInfo = getOAuth2UserInfo(providerType, user.attributes)
        var savedUser: Member? = memberRepository.findByEmail(userInfo.email!!)
        if (savedUser != null) {
            if (providerType !== savedUser.providerType) {
                throw OAuthProviderMissMatchException(
                    "Looks like you're signed up with " + providerType +
                            " account. Please use your " + savedUser.providerType + " account to login."
                )
            }
            if (userInfo.name != null) {
                savedUser.apply {
                    this.name = userInfo.name ?: ""
                    this.modifiedDate = LocalDateTime.now()
                }
            }
        } else {
            savedUser = Member(userInfo, providerType)
        }
        memberRepository.save(savedUser)
        return UserPrincipal.create(savedUser, user.attributes)
    }


}