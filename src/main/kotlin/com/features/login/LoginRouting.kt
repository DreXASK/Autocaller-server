package com.features.login

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureLoginRouting() {
    routing {
        get("/check_token_status"){

            val loginController = LoginController(call)
            loginController.performLogin()

        }
    }
}