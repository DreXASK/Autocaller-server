package com.plugins

import com.TokenStatus
import com.dto.TokenResponse
import com.dto.TokenStatusRequest
import com.dto.TokenStatusResponse
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        post("/register_client") {
            call.respond(TokenResponse("ClientToken123"))
        }
        get("/check_token_status"){

            val tokenStatusRequest = call.receive<TokenStatusRequest>()
            val token = tokenStatusRequest.token

            if (token.length >= 5 && token.substring(0..4) == "[E2E]")
                call.respond(TokenStatusResponse(TokenStatus.UNREGISTERED))
            else
                call.respond(TokenStatusResponse(TokenStatus.REGISTERED))
        }
    }
}
