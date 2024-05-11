package com.database.callTasks

import com.utils.OffsetDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.OffsetDateTime

@Serializable
data class CallTaskDto(
    val id: Long?,
    val surname: String?,
    val name: String?,
    val patronymic: String?,
    val phoneNumber: String,
    val messageText: String,
    val callAttempts: Int,
    @Serializable(with = OffsetDateTimeSerializer::class) val nextCallDateTimeUTC: OffsetDateTime
)