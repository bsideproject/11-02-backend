package com.bside.config.oauth

import com.bside.common.type.Authority
import com.bside.common.type.ProviderType
import com.bside.entity.Member
import org.bson.types.ObjectId
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.oauth2.core.oidc.OidcIdToken
import org.springframework.security.oauth2.core.oidc.OidcUserInfo
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.security.oauth2.core.user.OAuth2User

class UserPrincipal : OAuth2User, UserDetails, OidcUser {
    private var userId: String? = null
    private var password: String? = null
    private var providerType: ProviderType? = null
    private var authority: Authority? = null
    private var authorities: Collection<GrantedAuthority>? = null
    private var attributes: Map<String, Any>? = null

    override fun getAttributes(): Map<String, Any>? {
        return attributes
    }

    override fun getAuthorities(): Collection<GrantedAuthority?>? {
        return authorities
    }

    override fun getPassword(): String? {
        return password
    }

    override fun getName(): String? {
        return userId
    }

    override fun getUsername(): String? {
        return userId
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }

    override fun getClaims(): Map<String?, Any?>? {
        return null
    }

    override fun getUserInfo(): OidcUserInfo? {
        return null
    }

    override fun getIdToken(): OidcIdToken? {
        return null
    }
    companion object {
        fun create(member: Member): UserPrincipal {
            return UserPrincipal().apply {
                this.userId = member.email
                this.password = member.password
                this.providerType = member.providerType
                this.authority = Authority.ROLE_USER
                this.authorities = listOf(SimpleGrantedAuthority(Authority.ROLE_USER.toString()))
            }
        }


        fun create(
            member: Member,
            attributes: Map<String, Any>?
        ): UserPrincipal? {
            val userPrincipal: UserPrincipal = this.create(member)
            userPrincipal.attributes = attributes
            return userPrincipal
        }
    }
}