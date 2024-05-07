package com.database.callTasks

data class CallTasksDto(
    val id: Long?,
    val surname: String?,
    val name: String?,
    val patronymic: String?,
    val phoneNumber: String,
    val messageText: String,
    val callAttempts: Int,
    val nextCallDateAndTimeUTC: String
)