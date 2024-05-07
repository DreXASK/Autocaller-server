package com.features.callTasks

import com.database.tokens.Tokens
import com.features.login.LoginReceiveRemote
import com.features.login.LoginResponseRemote
import com.utils.ApiError
import com.utils.DataError
import com.utils.Result
import com.utils.TokenStatus
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

class CallTasksController(private val call: ApplicationCall) {

    suspend fun getCallTasksFromClient() {
        val receive = call.receive<CallTasksReceiveRemote>()

        receive.list

        when (val result = Tokens.fetch(receive.token)) {
            is Result.Success -> call.respond(LoginResponseRemote(TokenStatus.REGISTERED))
            is Result.Error -> {
                when (result.error) {
                    DataError.TokenError.TokenDoesNotExist -> {
                        call.respond(HttpStatusCode.BadRequest, ApiError.TokenStatusError.INVALID_TOKEN)
                    }
                }
            }
        }
    }

}