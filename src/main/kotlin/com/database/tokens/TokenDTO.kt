package com.database.tokens

import com.utils.TokenStatus

data class TokenDTO(
    val rowId: Long?,
    val token: String,
    val tokenStatus: TokenStatus
)