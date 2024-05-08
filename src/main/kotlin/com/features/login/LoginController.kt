package com.features.login

import com.database.tokens.Tokens
import com.utils.DataError
import com.utils.Result
import com.utils.TokenStatus
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

class LoginController(private val call: ApplicationCall) {

    suspend fun performLogin() {
        val receive = call.receive<LoginReceiveRemote>()

        when (val result = Tokens.fetch(receive.token)) {
            is Result.Success -> call.respond(LoginResponseRemote(TokenStatus.REGISTERED))
            is Result.Error -> {
                when (result.error) {
                    DataError.TokensError.TokensDoesNotExist -> {
                        call.respond(HttpStatusCode.BadRequest, "Invalid token")
                    }
                }
            }
        }
    }

}