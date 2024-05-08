package com.features.callTasks

import com.database.callTasks.CallTaskDto
import kotlinx.serialization.Serializable

@Serializable
data class GetCallTasksReceiveRemote(
    val token: String,
    val list: List<CallTaskDto>
)

@Serializable
data class RemoveCallTasksReceiveRemote(
    val token: String,
    val id: Long
)

@Serializable
data class SendCallTasksReceiveRemote(
    val token: String
)