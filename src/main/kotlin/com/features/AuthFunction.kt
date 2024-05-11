package com.features

import com.database.tokens.Tokens
import com.utils.DataError
import com.utils.Result
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

suspend fun auth(token: String, call: ApplicationCall): Boolean {
    return when (val result = Tokens.fetch(token)) {
        is Result.Success -> true
        is Result.Error -> {
            if (result.error == DataError.TokensError.TokensDoesNotExist)
                call.respond(HttpStatusCode.BadRequest, "Invalid token")
            false
        }
    }
}