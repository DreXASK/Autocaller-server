package com.database.callTasks

import kotlinx.serialization.Serializable

@Serializable
data class CallTaskForDeviceDto(
    val id: Long,
    val phoneNumber: String,
    val messageText: String,
    val callAttempts: Int
)