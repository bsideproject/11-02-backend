package com.bside.entity

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field



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
        var value: String = "",
)