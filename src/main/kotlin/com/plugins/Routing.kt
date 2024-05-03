package com.plugins

import com.ApiError
import com.TokenStatus
import com.dto.TokenRequest
import com.dto.TokenResponse
import com.dto.TokenStatusRequest
import com.dto.TokenStatusResponse
import io.ktor.client.plugins.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun Application.configureRouting() {
    routing {
        post("/register_client") {
            val connectionSecretKey = call.receive<TokenRequest>().connectionSecretKey

            if (connectionSecretKey == "secret)))")
                call.respond(TokenResponse("ClientToken123"))
            else
                call.respond(HttpStatusCode.BadRequest, Json.encodeToString(ApiError.TokenError.INVALID_CONNECTION_KEY))

        }
        get("/check_token_status"){

            val tokenStatusRequest = call.receive<TokenStatusRequest>()
            val token = tokenStatusRequest.token

            call.respond(TokenStatusResponse(TokenStatus.REGISTERED))
        }
    }
}
