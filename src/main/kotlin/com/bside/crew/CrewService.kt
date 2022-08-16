package com.bside.crew

import com.bside.common.type.ErrorMessage
import com.bside.crew.dto.request.CrewCreateRequest
import com.bside.crew.dto.response.CrewResponse
import com.bside.crew.entity.Crew
import com.bside.crew.reposittory.CrewRepository
import com.bside.error.exception.AlreadyExistException

import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class CrewService(val crewRepository: CrewRepository) {

    @Transactional
    fun save(userId: String, createCrewRequest: CrewCreateRequest): CrewResponse {
        if (crewRepository.existsByName(createCrewRequest.name)) {
            throw AlreadyExistException(
                    ErrorMessage.CREW_NAME_ALREADY_EXIST.name, ErrorMessage.CREW_NAME_ALREADY_EXIST.reason
            )
        }

        val createdCrew = crewRepository.save(
                Crew(
                        leaderId = ObjectId(userId),
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
}