package com.bside.feedImage.dto.reponse

import com.bside.feedImage.entity.FeedImage

data class FeedImageResponse(
        val id: String,
        val image: String
) {
    companion object {
        fun fromEntity(feedImage: FeedImage) = FeedImageResponse(id = feedImage.id.toString(), image = feedImage.image)
    }
}