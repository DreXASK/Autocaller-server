package com

import com.plugins.*

import io.ktor.server.application.*
import io.ktor.server.engine.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


suspend fun main() {

    var server: ApplicationEngine? = null
    val serverJob = CoroutineScope(Dispatchers.Default).launch {
        server = embeddedServer(io.ktor.server.cio.CIO, port = 8080, host = "0.0.0.0", module = Application::module)
        server?.start(wait = true) ?: throw Exception("Server is null")
    }

    CoroutineScope(Dispatchers.Default).launch {

        while(true) {
            val command = readln()
            when(command) {
                "authKey" -> println("The authentication key has been successfully created #rjejri23jrci23j")
                "lol" -> println("lol)")
                "close" -> server?.stop()
                else -> println("Command has not been recognized")
            }
        }

    }

    serverJob.join()
}

fun Application.module() {
    configureSerialization()
    configureRouting()
}
