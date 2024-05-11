package com.database.completedTasks

import com.utils.OffsetDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.OffsetDateTime

@Serializable
data class CompletedTaskDto(
    val id: Long?,
    val surname: String?,
    val name: String?,
    val patronymic: String?,
    val phoneNumber: String,
    val messageText: String,
    val callAttempts: Int,
    val isSmsUsed: Boolean,
    @Serializable(with = OffsetDateTimeSerializer::class) val informDateTimeUTC: OffsetDateTime
)