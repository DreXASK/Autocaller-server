package com.database.messageTemplates

import kotlinx.serialization.Serializable

@Serializable
data class MessageTemplatePlaceholdersDto(
    val isSurnamePlaceholderUsed: Boolean,
    val isNamePlaceholderUsed: Boolean,
    val isPatronymicPlaceholderUsed: Boolean,
    val isPhoneNumberPlaceholderUsed: Boolean,
    val isSexPlaceholderUsed: Boolean,
    val isAgePlaceholderUsed: Boolean
)