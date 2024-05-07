package com.features.getTasks

import com.utils.TokenStatus
import kotlinx.serialization.Serializable

//@Serializable
//data class GetTasksReceiveRemote(
//    val list: List<TaskData>,
//    val token: String
//)

@Serializable
data class GetTasksResponseRemote(
    val tokenStatus: TokenStatus
)