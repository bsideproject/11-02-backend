package com.bside.crew.dto.response

import com.bside.activity.dto.response.ActivityResponse
import com.bside.activity.entity.Activity
import com.bside.common.dto.request.CursorPageable
import com.bside.common.dto.response.PageDto
import com.bside.crew.entity.Crew
import com.bside.feedImage.dto.reponse.FeedImageResponse
import com.bside.feedImage.entity.FeedImage
import com.bside.member.dto.response.MemberResponse
import com.bside.member.entity.Member

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
        val activities: List<ActivityResponse>? = null,
        val feedImages: List<FeedImageResponse>? = null,
        val memberProfiles: List<MemberResponse>? = null,
) {
    companion object {
        fun fromEntity(crew: Crew, members: List<Member>? = null, feeds: List<FeedImage>? = null, activities: List<Activity>? = null): CrewResponse = CrewResponse(
                id = crew.id.toString(),
                leaderId = crew.leaderId.toString(),
                memberIds = crew.memberIds.map { it.toString() },
                name = crew.name,
                title = crew.title,
                description = crew.description,
                capacity = crew.capacity,
                joinCount = crew.joinCount,
                mainImage = crew.mainImage,
                location = crew.location,
                memberProfiles = members?.map { MemberResponse.fromEntity(it) },
                activities = activities?.map { ActivityResponse.fromEntity(it) },
                feedImages = feeds?.map { FeedImageResponse.fromEntity(it) },
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
