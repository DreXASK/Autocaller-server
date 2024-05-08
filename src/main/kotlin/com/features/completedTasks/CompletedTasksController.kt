package com.features.completedTasks

import com.database.callTasks.CallTasks
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