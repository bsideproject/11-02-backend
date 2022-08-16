package com.bside.feedImage.entity

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

@Document
data class FeedImage(
        @Id
        @Field("_id")
        val id: ObjectId = ObjectId.get(),
        val memberId: ObjectId,
        val crewId: ObjectId,
        val image: String
)
