package com.dto

import kotlinx.serialization.Serializable

@Serializable
data class TokenRequest(
	val token: String
)

@Serializable
data class TokenResponse(
	val token: String
)