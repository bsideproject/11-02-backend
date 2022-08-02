package com.bside.scheduler

import com.bside.service.ArticleService
import com.bside.util.logger
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.scheduling.annotation.Schedules
import org.springframework.stereotype.Component

@Component
@EnableScheduling
class Scheduler(
    val articleService: ArticleService
) {

    private val logger by logger()

    //@Scheduled(cron = "0 0 14 * * *")
    @Scheduled(cron = "0 08 20 * * *")
    fun articleJob() {
        logger.info("articleJob start")
        articleService.scheduledArticle()
        logger.info("articleJob end")
    }
}