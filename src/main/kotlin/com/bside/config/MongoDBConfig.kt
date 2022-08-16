package com.bside.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.MongoDatabaseFactory
import org.springframework.data.mongodb.MongoTransactionManager
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory


@Configuration
class MongoDBConfig {
    @Value("\${spring.bside.mongodb.url}")
    private lateinit var url: String

    // https://www.baeldung.com/spring-data-mongodb-transactions
    // 기본적으로 spring data mongodb 는 트랜잭션이 비활성화 되어있기 때문에
    // MongoTransactionManager 빈을 등록하여 트랜잭션 활성화
    @Bean
    fun transactionManager(mongoDatabaseFactory: MongoDatabaseFactory): MongoTransactionManager {
        return MongoTransactionManager(mongoDatabaseFactory)
    }

    @Bean
    fun mongoDatabaseFactory(): MongoDatabaseFactory {
        return SimpleMongoClientDatabaseFactory(url)
    }

    @Bean
    fun mongoTemplate(mongoDatabaseFactory: MongoDatabaseFactory): MongoTemplate {
        return MongoTemplate(mongoDatabaseFactory);
    }
}