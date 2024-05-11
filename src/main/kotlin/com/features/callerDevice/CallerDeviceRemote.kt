package com.features.callerDevice

import com.database.callTasks.CallTaskForDeviceDto
import kotlinx.serialization.Serializable


@Serializable
data class ResultCallTaskReceiveRemote(
    val token: String,
    val callTaskId: Long,
    val isSuccess: Boolean
)

@Serializable
data class SendOlderCallTaskReceiveRemote(
    val token: String
)