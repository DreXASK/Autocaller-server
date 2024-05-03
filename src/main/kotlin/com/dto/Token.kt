package com.dto

import kotlinx.serialization.Serializable

@Serializable
data class TokenRequest(
	val connectionSecretKey: String
)

@Serializable
data class TokenResponse(
	val token: String
)