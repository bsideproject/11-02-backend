package com.bside.entity

import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

import javax.persistence.Id


/**
 * name : RefreshToken
 * author : jisun.noh
 */
@Document
data class RefreshToken(
        @Id
        @Field(name = "_id")
        val id: ObjectId = ObjectId.get(),
        @Field(name = "rt_key")
        val key: String = "",
        @Field(name = "rt_value")
        val value: String = "",
)