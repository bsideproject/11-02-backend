package com.bside.member.entity

import com.bside.common.type.Authority
import com.bside.common.type.ProviderType
import com.bside.config.oauth.OAuth2UserInfo
import com.bside.member.type.Gender

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

import java.time.LocalDateTime

@Document
data class Member(
        @Id
        @Field(name = "_id")
        val id: ObjectId = ObjectId.get(),
        val email: String = "",
        val password: String = "",
        var name: String = "",
        var nickname: String? = "",
        val authority: Authority = Authority.ROLE_USER,
        val providerType: ProviderType,
        var profile: String? = "",
        var gender: Gender? = null,
        var birthday: String? = null,
        val createdDate: LocalDateTime = LocalDateTime.now(),
        var modifiedDate: LocalDateTime = LocalDateTime.now()
) {
        constructor(userInfo: OAuth2UserInfo, providerType: ProviderType) : this(
                email = userInfo.email!!,
                name = userInfo.name!!,
                authority = Authority.ROLE_USER,
                providerType = providerType,
                createdDate = LocalDateTime.now()
        )

}