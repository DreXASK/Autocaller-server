package com.features.register

import com.database.tokens.TokenDTO
import com.database.tokens.Tokens
import com.utils.TokenStatus
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import java.util.UUID

class RegisterController(private val call: ApplicationCall) {

    suspend fun performRegister() {

            val receive = call.receive<RegisterReceiveLocal>()

            println("Received secret key = ${receive.connectionSecretKey}")

            val token = UUID.randomUUID().toString()
            val tokenDTO = TokenDTO(rowId = null, token = token, tokenStatus = TokenStatus.REGISTERED )

            Tokens.insert(tokenDTO)
            call.respond(RegisterResponseLocal(token))

    }

}