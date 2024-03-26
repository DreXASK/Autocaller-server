package com.plugins

import com.ServerConnectionStatus
import com.dto.AdminTokenResponse
import com.dto.ClientTokenResponse
import com.dto.ClientTokenStatusResponse
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/registerClient") {
            call.respond(ClientTokenResponse("ClientToken123"))
        }
        get("/check_token_status"){
            call.respond(ClientTokenStatusResponse(ServerConnectionStatus.Connected))
        }
        get("/admin_authorization"){
            call.respond(AdminTokenResponse("AdminToken123"))
        }
    }
}
