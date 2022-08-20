package com.bside.crew

import com.bside.common.dto.request.CursorPageable
import com.bside.common.dto.response.PageDto
import com.bside.common.type.ErrorMessage
import com.bside.crew.dto.request.CrewCreateRequest
import com.bside.crew.dto.response.CrewResponse
import com.bside.crew.entity.Crew
import com.bside.crew.reposittory.CrewRepository
import com.bside.error.exception.AlreadyExistException
import com.bside.error.exception.NotExistException

import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import java.time.LocalDateTime


@Service
class CrewService(val crewRepository: CrewRepository) {
    @Transactional(readOnly = true)
    fun findAll(cursorPageable: CursorPageable): PageDto<CrewResponse> {
        val crewList = crewRepository.findAll(cursorPageable)
        return CrewResponse.toPageDtoFromEntity(crewList, cursorPageable)
    }

    @Transactional(readOnly = true)
    fun findAllByMemberId(memberId: String): List<CrewResponse> {
        val crews = crewRepository.findByMemberIds(mutableListOf(ObjectId(memberId)))
        return crews.map { CrewResponse.fromEntity(it) }
    }

    @Transactional
    fun save(memberId: String, createCrewRequest: CrewCreateRequest): CrewResponse {
        if (crewRepository.existsByName(createCrewRequest.name)) {
            throw AlreadyExistException(
                    ErrorMessage.CREW_NAME_ALREADY_EXIST.name, ErrorMessage.CREW_NAME_ALREADY_EXIST.reason
            )
        }

        val createdCrew = crewRepository.save(
                Crew(
                        leaderId = ObjectId(memberId),
                        memberIds = listOf(ObjectId(memberId)),
                        name = createCrewRequest.name,
                        title = createCrewRequest.title,
                        description = createCrewRequest.description,
                        capacity = createCrewRequest.capacity,
                        mainImage = createCrewRequest.mainImage,
                        location = createCrewRequest.location
                )
        )

        return CrewResponse.fromEntity(createdCrew)
    }

    @Transactional
    fun join(crewId: String, memberId: String): CrewResponse {
        val crew = crewRepository
                .findById(crewId)
                .orElseThrow { throw NotExistException(ErrorMessage.CREW_NOT_EXIST.name, ErrorMessage.CREW_NOT_EXIST.reason) }

        if (crew.memberIds.contains(ObjectId(memberId)))
            throw AlreadyExistException(ErrorMessage.ALREADY_JOINED_USER.name, ErrorMessage.ALREADY_JOINED_USER.reason)

        if (crew.joinCount >= crew.capacity)
            throw AlreadyExistException(ErrorMessage.ALREADY_FULL_CAPACITY_CREW.name, ErrorMessage.ALREADY_FULL_CAPACITY_CREW.reason)

        val updatedCrew = crewRepository.save(
                crew.copy(
                        memberIds = crew.memberIds + ObjectId(memberId),
                        joinCount = crew.joinCount + 1,
                        modifiedDate = LocalDateTime.now(),
                ))
        return CrewResponse.fromEntity(updatedCrew)
    }
}
