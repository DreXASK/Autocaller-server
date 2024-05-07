package com.features.callTasks

import kotlinx.serialization.Serializable

@Serializable
data class CallTasksReceiveRemote(
    val token: String,
    val list: List<CallTaskDataFromJson>
)

//@Serializable
//data class CallTasksResponseRemote(
//    val tokenStatus: TokenStatus
//)


@Serializable
data class CallTaskDataFromJson(
    val surname: String?,
    val name: String?,
    val patronymic: String?,
    val phoneNumber: String,
    val messageText: String,
    val callAttempts: Int,
    val nextCallDateAndTimeUTC: String
)