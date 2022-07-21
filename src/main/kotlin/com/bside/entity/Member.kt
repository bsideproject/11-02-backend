package com.bside.entity

import com.bside.common.type.Authority
import com.bside.common.type.ProviderType

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

import java.time.LocalDateTime

/**
 * name : Member
 * author : jisun.noh
 */
@Document
data class Member(
        @Id
        @Field(name = "_id")
        val id: ObjectId = ObjectId.get(),
        val email: String = "",
        val password: String = "",
        var name: String = "",
        val authority: Authority = Authority.ROLE_USER,
        val providerType: ProviderType,
        val createdDate: LocalDateTime = LocalDateTime.now(),
        val modifiedDate: LocalDateTime = LocalDateTime.now()
)