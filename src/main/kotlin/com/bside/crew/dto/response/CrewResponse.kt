package com.bside.crew.dto.response

import com.bside.common.dto.request.CursorPageable
import com.bside.common.dto.response.PageDto
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

        fun toPageDtoFromEntity(crewList: List<Crew>, cursorPageable: CursorPageable): PageDto<CrewResponse> {
            val entities = crewList.map { fromEntity(it) }.toMutableList()
            if (isMorePageData(crewList, cursorPageable.size)) {
                entities.removeLast()
                return PageDto(entities = entities, lastId = entities.lastOrNull()?.id.toString())
            }

            return PageDto(entities = entities)
        }

        private fun isMorePageData(crewList: List<Crew>, pageSize: Int): Boolean {
            return crewList.isNotEmpty() && crewList.size == pageSize + 1
        }
    }
}
