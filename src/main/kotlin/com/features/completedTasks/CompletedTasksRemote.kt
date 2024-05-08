package com.features.completedTasks

import com.database.callTasks.CallTaskDto
import com.database.completedTasks.CompletedTaskDto
import kotlinx.serialization.Serializable

@Serializable
data class SendCompletedTasksReceiveRemote(
    val token: String
)

@Serializable
data class GetCompletedTasksReceiveRemote(
    val token: String,
    val list: List<CompletedTaskDto>
)

