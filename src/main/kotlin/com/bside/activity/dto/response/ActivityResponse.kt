package com.bside.activity.dto.response

import com.bside.activity.entity.Activity
import com.bside.activity.entity.Location
import com.bside.common.type.ActivityStatus
import org.bson.types.ObjectId
import java.time.LocalDateTime

data class ActivityResponse(
        val id: String,
        val leaderId: ObjectId,
        val crewId: ObjectId,
        val name: String,
        val description: String,
        val capacity: Int,
        val joinCount: Int = 1,
        val location: Location,
        val status: ActivityStatus = ActivityStatus.READY,
        val startAt: LocalDateTime,
) {
    companion object {
        fun fromEntity(activity: Activity) = ActivityResponse(
                id = activity.id.toString(),
                leaderId = activity.leaderId,
                crewId = activity.crewId,
                name = activity.name,
                description = activity.description,
                location = activity.location,
                capacity = activity.capacity,
                joinCount = activity.joinCount,
                startAt = activity.startAt,
                status = ActivityStatus.READY)
    }
}
