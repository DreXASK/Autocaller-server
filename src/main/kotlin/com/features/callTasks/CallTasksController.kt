package com.features.callTasks

import com.database.callTasks.CallTaskDto
import com.database.callTasks.CallTasks
import com.database.tokens.Tokens
import com.utils.ApiError
import com.utils.DataError
import com.utils.Result
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import java.time.OffsetDateTime

class CallTasksController(private val call: ApplicationCall) {

    suspend fun getCallTasksFromClient() {
        val receive = call.receive<GetCallTasksReceiveRemote>()

        when (val result = Tokens.fetch(receive.token)) {
            is Result.Error -> {
                if (result.error == DataError.TokenError.TokenDoesNotExist)
                    call.respond(HttpStatusCode.BadRequest, ApiError.TokenStatusError.INVALID_TOKEN)
            }

            is Result.Success -> Unit
        }

        val callTaskDtoList = receive.list
        when (val result = CallTasks.insert(callTaskDtoList)) {
            is Result.Success -> call.respond(HttpStatusCode.OK)
            is Result.Error -> {
                when (result.error) {
                    is DataError.CallTaskError.Insert.UnknownError ->
                        call.respond(
                            HttpStatusCode.BadRequest,
                            ApiError.CallTasksError.Remote.UnknownError(result.error.e)
                        )
                }
            }
        }
    }

    suspend fun sendCallTasksToClient() {
        val receive = call.receive<SendCallTasksReceiveRemote>()

        auth(receive.token)

        when (val result = CallTasks.fetchAll()) {
            is Result.Success -> call.respond(result.data)
            is Result.Error -> {
                when (result.error) {
                    is DataError.CallTaskError.Fetch.CallTaskDoesNotExist ->
                        call.respond(HttpStatusCode.BadRequest, ApiError.CallTasksError.Remote.UnknownError(null))
                }
            }
        }
    }

    suspend fun removeCallTask() {
        val receive = call.receive<RemoveCallTasksReceiveRemote>()

        auth(receive.token)

        when (val result = CallTasks.remove(receive.id)) {
            is Result.Success -> call.respond(HttpStatusCode.OK)
            is Result.Error -> {
                when (result.error) {
                    is DataError.CallTaskError.Remove.UnknownError -> call.respond(
                        HttpStatusCode.BadRequest,
                        ApiError.CallTasksError.Remote.UnknownError(result.error.e)
                    )
                }
            }
        }
    }

    private suspend fun auth(token: String) {
        when (val result = Tokens.fetch(token)) {
            is Result.Error -> {
                if (result.error == DataError.TokenError.TokenDoesNotExist)
                    call.respond(HttpStatusCode.BadRequest, ApiError.TokenStatusError.INVALID_TOKEN)
            }

            is Result.Success -> Unit
        }
    }
}