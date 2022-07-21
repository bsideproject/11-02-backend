package com.bside.service

import com.bside.common.type.Authority
import com.bside.common.type.ProviderType
import com.bside.config.oauth.OAuth2UserInfo
import com.bside.config.oauth.exception.OAuthProviderMissMatchException
import com.bside.config.oauth.info.Oauth2UserInfoFactory.Companion.getOAuth2UserInfo
import com.bside.config.oauth.UserPrincipal
import com.bside.entity.Member
import com.bside.repository.MemberRepository
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
            updateUser(savedUser, userInfo)
        } else {
            savedUser = createUser(userInfo, providerType)
        }
        return UserPrincipal.create(savedUser, user.attributes)
    }

    private fun createUser(userInfo: OAuth2UserInfo, providerType: ProviderType): Member {
        val now = LocalDateTime.now()
        val member = Member(
            email = userInfo.email!!,
            name = userInfo.name!!,
            authority = Authority.ROLE_USER,
            providerType = providerType,
            createdDate = now
        )
        return memberRepository.save(member)
    }

    private fun updateUser(member: Member, userInfo: OAuth2UserInfo): Member {
        if (userInfo.name != null) {
            member.name = userInfo.name!!
        }
        return member
    }
}