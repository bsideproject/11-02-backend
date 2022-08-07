package com.bside.scheduler

import com.bside.article.ArticleService
import com.bside.util.logger
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
@EnableScheduling
class ArticleScheduler(
    val articleService: ArticleService
) {

    private val logger by logger()

    @Scheduled(cron = "0 0 14 * * *")
    fun articleJob() {
        logger.info("articleJob start")
        articleService.scheduledArticle()
        logger.info("articleJob end")
    }
}