package com.bside.feedImage.repository

import com.bside.feedImage.entity.FeedImage

import org.springframework.data.mongodb.repository.MongoRepository

interface FeedImageRepository: MongoRepository<FeedImage, String> {
}