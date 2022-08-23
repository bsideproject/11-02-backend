package com.bside.crew.reposittory

import com.bside.common.dto.request.CursorPageable
import com.bside.crew.entity.Crew

import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Criteria.where
import org.springframework.data.mongodb.core.query.Query

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query as RQuery

interface CrewRepositoryCustom {
    fun findAll(cursorPageable: CursorPageable): List<Crew>

}

class CrewRepositoryImpl : CrewRepositoryCustom {
    @Autowired
    lateinit var mongoTemplate: MongoTemplate
    override fun findAll(cursorPageable: CursorPageable): List<Crew> {
        val criteria = getCursorPageableCriteria(cursorPageable)
        return mongoTemplate.find(
                Query.query(criteria)
                        .with(Sort.by(cursorPageable.direction, "_id"))
                        .limit(cursorPageable.size + 1), // 다음 페이지의 데이터가 존재하는지 확인하기 위해 size + 1 개 조회
                Crew::class.java)
    }

    private fun getCursorPageableCriteria(cursorPageable: CursorPageable): Criteria {
        if (cursorPageable.lastId == null) return Criteria()
        if (cursorPageable.direction == Sort.Direction.DESC) return where("_id").`lt`(ObjectId(cursorPageable.lastId))
        if (cursorPageable.direction == Sort.Direction.ASC) return where("_id").`gt`(ObjectId(cursorPageable.lastId))
        throw RuntimeException("Invalid cursorPageable factor. $cursorPageable")
    }
}

interface CrewRepository : MongoRepository<Crew, String>, CrewRepositoryCustom {
    fun existsByName(name: String): Boolean

    @RQuery(value = "{ 'memberIds' : {\$all : ?0 }}")
    fun findByMemberIds(memberIds: MutableList<ObjectId>): List<Crew>
}
