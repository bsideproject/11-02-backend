package com.bside.activity.entity

import com.bside.common.type.ActivityStatus

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

import java.time.LocalDateTime


data class Location(
        val lat: Double,
        val lon: Double
)

@Document
data class Activity(
        @Id
        @Field(name = "_id")
        val id: ObjectId = ObjectId.get(),
        val leaderId: ObjectId,
        val crewId: ObjectId,
        val memberIds: List<ObjectId> = listOf(),
        val name: String,
        val description: String,
        val capacity: Int,
        val joinCount:Int = 1,
        val mainImage: String,
        val location: Location,
        val status: ActivityStatus = ActivityStatus.READY,
        val startAt: LocalDateTime,
        val createdDate: LocalDateTime = LocalDateTime.now(),
        val modifiedDate: LocalDateTime = LocalDateTime.now()
)

