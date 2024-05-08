package com.features.messageTemplates

import com.database.callTasks.CallTaskDto
import com.database.messageTemplates.MessageTemplateDto
import kotlinx.serialization.Serializable

@Serializable
data class GetMessageTemplateReceiveRemote(
    val token: String,
    val messageTemplate: MessageTemplateDto
)

@Serializable
data class RemoveMessageTemplateReceiveRemote(
    val token: String,
    val id: Long
)

@Serializable
data class SendMessageTemplatesReceiveRemote(
    val token: String
)