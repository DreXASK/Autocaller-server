package com

import com.dto.ClientTokenResponse
import com.plugins.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*

import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


fun main() {
    CoroutineScope(Dispatchers.Default).launch {

        delay(5000)

        val httpClient = HttpClient(CIO) {
            //    install(Logging) { level = LogLevel.ALL }
            install(ContentNegotiation) {
                json()
            }
        }

        val token = httpClient.get {
            url("http://localhost:8080/register_client")
        }.body<ClientTokenResponse>().token

        println(token)

    }

    embeddedServer(io.ktor.server.cio.CIO, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)

}

fun Application.module() {
    configureSerialization()
    configureRouting()
}
