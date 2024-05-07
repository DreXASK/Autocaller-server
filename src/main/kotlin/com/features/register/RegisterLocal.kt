package com.features.register

import kotlinx.serialization.Serializable

@Serializable
data class RegisterReceiveLocal(
	val connectionSecretKey: String
)

@Serializable
data class RegisterResponseLocal(
	val token: String
)