package com.database.callTasksInWork

import com.utils.OffsetDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.OffsetDateTime

@Serializable
data class CallTaskInWorkDto(
    val id: Long,
    @Serializable(with = OffsetDateTimeSerializer::class) val dateTimeOfFetchUTC: OffsetDateTime
)