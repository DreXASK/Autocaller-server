package com.database.messageTemplates

import kotlinx.serialization.Serializable

@Serializable
data class MessageTemplateDto(
    val id: Long?,
    val name: String,
    val text: String,
    val placeholders: MessageTemplatePlaceholdersDto
)