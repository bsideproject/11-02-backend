package com.bside.repository

import com.bside.entity.RefreshToken

import org.springframework.data.mongodb.repository.MongoRepository

/**
 * name : TokenRepository
 * author : jisun.noh
 */
interface TokenRepository : MongoRepository<RefreshToken, String> {
    fun findByKey(key: String): RefreshToken?
}