package com.bside.repository

import com.bside.entity.RefreshToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


/**
 * name : TokenRepository
 * author : jisun.noh
 */
@Repository
interface TokenRepository : JpaRepository<RefreshToken, Long> {
    fun findByKey(key: String): RefreshToken
}