package com.bside.crew.entity

import org.bson.types.ObjectId

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

import java.time.LocalDateTime

// 가입 인삿말 필드 추가 필요.
@Document
data class Crew(
        @Id
        @Field(name = "_id")
        val id: ObjectId = ObjectId.get(),
        val leaderId: ObjectId,
        val memberIds: List<ObjectId> = listOf(),
        val name: String,
        val title: String,
        val description: String,
        val capacity: Int,
        val joinCount:Int = 1,
        val mainImage: String,
        val location: Map<String, List<String>>,
        val createdDate: LocalDateTime = LocalDateTime.now(),
        val modifiedDate: LocalDateTime = LocalDateTime.now()
)
