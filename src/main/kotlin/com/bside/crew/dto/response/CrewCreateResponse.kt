package com.bside.crew.dto.response

import com.bside.crew.entity.Crew

data class CrewResponse(
        val id: String,
        val leaderId: String,
        val memberIds: List<String> = listOf(),
        val name: String,
        val title: String,
        val description: String,
        val capacity: Int,
        val joinCount: Int = 1,
        val mainImage: String,
        val location: Map<String, List<String>>,
) {
    companion object {
        fun fromEntity(crew: Crew): CrewResponse =
                CrewResponse(
                        id = crew.id.toString(),
                        leaderId = crew.leaderId.toString(),
                        memberIds = crew.memberIds.map { it.toString() },
                        name = crew.name,
                        title = crew.title,
                        description = crew.description,
                        capacity = crew.capacity,
                        joinCount = crew.joinCount,
                        mainImage = crew.mainImage,
                        location = crew.location
                )
    }
}
