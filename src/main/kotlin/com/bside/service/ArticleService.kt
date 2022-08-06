package com.bside.service

import com.bside.dto.response.ArticleResponseDto
import com.bside.entity.Article
import com.bside.repository.ArticleRepository
import com.bside.util.logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.UriComponentsBuilder
import reactor.core.publisher.Mono
import reactor.netty.http.client.HttpClient
import java.util.*

@Service
class ArticleService(
    @Value("\${naver.api.client}") val clientId: String,
    @Value("\${naver.api.secret}") val secretKey: String,
    val articleRepository: ArticleRepository
) {
    private val logger by logger()

    fun scheduledArticle() {
        val url = UriComponentsBuilder
            .fromUriString("https://openapi.naver.com/v1/search/news.json")
            .queryParam("query", "플로깅")
            .queryParam("display", "20")
            .queryParam("start", "1")
            .queryParam("sort","sim")
            .build()
            .toUriString()

        val articleResponseDto = WebClient.create()
            .get()
            .uri(url)
            .header("X-Naver-Client-Id", clientId)
            .header("X-Naver-Client-Secret", secretKey)
            .retrieve()
            .onStatus(HttpStatus::isError) {t -> Mono.error(Exception(t.toString()))}
            .bodyToMono(ArticleResponseDto::class.java)
            .block()

        if(articleResponseDto != null) {
            articleResponseDto.items?.forEach {
                if (!articleRepository.existsByLink(it.link)) {
                    articleRepository.save(
                        Article(
                            title = it.title,
                            link = it.link,
                            descrition = it.description,
                            pubDate = it.pubDate ?: Date()
                        )
                    )
                }
            }
        }

    }


}