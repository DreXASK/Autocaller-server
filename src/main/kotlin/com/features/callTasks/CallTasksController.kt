package com.features.callTasks

import com.database.callTasks.CallTasks
import com.database.tokens.Tokens
import com.utils.ApiError
import com.utils.DataError
import com.utils.Result
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

class CallTasksController(private val call: ApplicationCall) {

    suspend fun getCallTasksFromClient() {
        val receive = call.receive<GetCallTasksReceiveRemote>()

        auth(receive.token)

        val callTaskDtoList = receive.list
        when (val result = CallTasks.insert(callTaskDtoList)) {
            is Result.Success -> call.respond(HttpStatusCode.OK)
            is Result.Error -> {
                when (result.error) {
                    is DataError.CallTasksError.Insert.UnknownError ->
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
                    is DataError.CallTasksError.Fetch.CallTasksDoesNotExist ->
                        call.respond(HttpStatusCode.BadRequest, ApiError.CallTasksError.Remote.UnknownError(null))
                }
            }
        }
    }

    suspend fun removeCallTask() {
        val receive = call.receive<RemoveCallTaskReceiveRemote>()

        auth(receive.token)

        when (val result = CallTasks.remove(receive.id)) {
            is Result.Success -> call.respond(HttpStatusCode.OK)
            is Result.Error -> {
                when (result.error) {
                    is DataError.CallTasksError.Remove.UnknownError -> call.respond(
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
                if (result.error == DataError.TokensError.TokensDoesNotExist)
                    call.respond(HttpStatusCode.BadRequest, ApiError.TokenStatusError.INVALID_TOKEN)
            }

            is Result.Success -> Unit
        }
    }
}