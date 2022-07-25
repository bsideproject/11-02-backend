package com.bside.repository

import com.bside.entity.Member
import org.bson.types.ObjectId

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.core.query.Criteria.where

/**
 * deleteByEmail 는 임시 코드입니다.
 */
interface MemberRepositoryCustom {
    fun deleteByEmail(email: String): List<Member>
}

class  MemberRepositoryImpl : MemberRepositoryCustom {
    @Autowired
    lateinit var mongoTemplate: MongoTemplate

    override fun deleteByEmail(email: String): List<Member> =
            mongoTemplate.findAllAndRemove(Query()
                    .addCriteria(
                            where("email").`is`(email)
                    ), Member::class.java)

}

/**
 * name : MemberRepository
 * author : jisun.noh
 */
interface MemberRepository : MongoRepository<Member, String>, MemberRepositoryCustom {
    fun findByEmail(email: String): Member?
    fun existsByEmail(email: String): Boolean

}