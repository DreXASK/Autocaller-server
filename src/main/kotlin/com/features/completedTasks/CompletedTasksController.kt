package com.features.completedTasks

import com.database.completedTasks.CompletedTasks
import com.database.tokens.Tokens
import com.utils.ApiError
import com.utils.DataError
import com.utils.Result
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

class CompletedTasksController(private val call: ApplicationCall) {

    suspend fun sendCallTasksToClient() {
        val receive = call.receive<SendCompletedTasksReceiveRemote>()

        auth(receive.token)

        when (val result = CompletedTasks.fetchAll()) {
            is Result.Success -> call.respond(result.data)
            is Result.Error -> {
                when (result.error) {
                    is DataError.CompletedTasksError.Fetch.CallTasksDoesNotExist ->
                        call.respond(HttpStatusCode.BadRequest, ApiError.CompletedTasksError.Remote.UnknownError(null))
                }
            }
        }
    }

    suspend fun getCallTasksFromClient() {
        val receive = call.receive<GetCompletedTasksReceiveRemote>()

        auth(receive.token)

        val completedTaskDtoList = receive.list
        when (val result = CompletedTasks.insert(completedTaskDtoList)) {
            is Result.Success -> call.respond(HttpStatusCode.OK)
            is Result.Error -> {
                when (result.error) {
                    is DataError.CompletedTasksError.Insert.UnknownError ->
                        call.respond(
                            HttpStatusCode.BadRequest,
                            ApiError.CompletedTasksError.Remote.UnknownError(result.error.e)
                        )
                }
            }
        }
    }

    private suspend fun auth(token: String) {
        when (val result = Tokens.fetch(token)) {
            is Result.Error -> {
                if (result.error == DataError.TokensError.TokensDoesNotExist)
                    call.respond(HttpStatusCode.BadRequest, ApiError.TokenStatusError.INVALID_TOKEN)
            }

            is Result.Success -> Unit
        }
    }

}