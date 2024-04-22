package com.dto

import com.TokenStatus
import kotlinx.serialization.Serializable

@Serializable
data class TokenStatusRequest(
	val token: String
)

@Serializable
data class TokenStatusResponse(
	val tokenStatus: TokenStatus
)
