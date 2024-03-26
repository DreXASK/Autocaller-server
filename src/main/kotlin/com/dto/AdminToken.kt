package com.dto

import kotlinx.serialization.Serializable
@Serializable
data class AdminTokenResponse(
	val adminToken: String
)