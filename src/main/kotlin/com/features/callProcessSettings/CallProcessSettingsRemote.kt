package com.features.callProcessSettings

import com.database.callProcessSettings.CallProcessSettingsDto
import com.database.callTasks.CallTaskDto
import kotlinx.serialization.Serializable

@Serializable
data class GetCallProcessSettingsReceiveRemote(
    val token: String,
    val settings: CallProcessSettingsDto
)

@Serializable
data class SendCallProcessSettingsReceiveRemote(
    val token: String
)