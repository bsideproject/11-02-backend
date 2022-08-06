package com.bside.repository

import com.bside.entity.Article
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ArticleRepository: MongoRepository<Article, String> {
    fun existsByLink(link: String): Boolean
}