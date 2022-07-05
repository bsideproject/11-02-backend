package com.bside.repository

import com.bside.entity.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


/**
 * name : MemberReposiroty
 * author : jisun.noh
 */
@Repository
interface MemberReposiroty : JpaRepository<Member, Long> {
    fun findByEmail(email: String): Member
    fun existsByEmail(email: String): Boolean
}