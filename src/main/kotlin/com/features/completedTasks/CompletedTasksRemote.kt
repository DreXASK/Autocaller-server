package com.features.completedTasks

import com.database.callTasks.CallTaskDto
import kotlinx.serialization.Serializable

@Serializable
data class SendCompletedTasksReceiveRemote(
    val token: String
)