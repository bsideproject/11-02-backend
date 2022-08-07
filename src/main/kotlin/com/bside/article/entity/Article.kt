package com.bside.article.entity

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.util.*

@Document
data class Article(
    @Id
    @Field(name = "_id")
    val id: ObjectId = ObjectId.get(),
    val title: String = "",
    val link: String = "",
    val descrition: String = "",
    val pubDate: Date
)
