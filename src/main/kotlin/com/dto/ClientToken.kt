package com.dto

import com.ServerConnectionStatus
import kotlinx.serialization.Serializable

@Serializable
data class ClientTokenRequest(
	val token: String
)

@Serializable
data class ClientTokenResponse(
	val token: String
)