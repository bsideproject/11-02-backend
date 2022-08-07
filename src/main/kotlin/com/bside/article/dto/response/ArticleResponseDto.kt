package com.bside.article.dto.response

import java.util.*

data class ArticleResponseDto (
    val total: Int = 0,
    val start: Int = 0,
    val display: Int = 0,
    val items: List<ArticleItem>? = null
)

data class ArticleItem (
    val title: String = "",
    val link: String = "",
    val description: String = "",
    val pubDate: Date? = null
)