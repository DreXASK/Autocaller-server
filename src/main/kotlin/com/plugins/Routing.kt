package com.plugins

import com.ServerConnectionStatus
import connectionAdjusterScreen.data.remote.dto.TokenRequest
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/registerClient") {
            call.respond(TokenRequest("123"))
        }
        get("/check_token_status"){
            call.respond(ServerConnectionStatus.Connected)
        }
    }
}
