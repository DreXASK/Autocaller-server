package com.features.register

import com.utils.ApiError
import com.utils.TokenStatus
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.UUID

fun Application.configureRegisterRouting() {
    routing {
        post("/register_client") {
            val registerController = RegisterController(call)
            registerController.performRegister()
        }
    }
}