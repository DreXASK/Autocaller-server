package com.features.callerDevice

import kotlinx.serialization.Serializable


@Serializable
data class ResultCallTaskReceiveRemote(
    val token: String,
    val id: Long,
    val isSuccess: Boolean
)

@Serializable
data class SendOlderCallTaskReceiveRemote(
    val token: String
)