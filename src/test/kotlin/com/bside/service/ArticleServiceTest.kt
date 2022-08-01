package com.bside.service

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest


@SpringBootTest
class ArticleServiceTest{

    @Autowired
    private lateinit var articleService: ArticleService

    @Test
    fun scscheduledArticleTest() {
        articleService.scheduledArticle()
    }
}