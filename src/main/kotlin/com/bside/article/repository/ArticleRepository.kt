package com.bside.article.repository

import com.bside.article.entity.Article
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ArticleRepository: MongoRepository<Article, String> {
    fun existsByLink(link: String): Boolean
}