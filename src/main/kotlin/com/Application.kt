package com

import com.database.tokens.Tokens
import com.features.callTasks.configureCallTasksRouting
import com.features.login.configureLoginRouting
import com.features.messageTemplates.configureMessageTemplatesRouting
import com.features.register.RegisterController
import com.plugins.*
import com.utils.DataError
import com.utils.Result

import io.ktor.server.application.*
import io.ktor.server.engine.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.exposed.sql.Database
import java.time.*


suspend fun main() {

    Database.connect("jdbc:postgresql://localhost:5432/autocaller", driver = "org.postgresql.Driver",
        user = "postgres", password = "admin")

    var server: ApplicationEngine? = null
    val serverJob = CoroutineScope(Dispatchers.Default).launch {
        server = embeddedServer(io.ktor.server.cio.CIO, port = 8080, host = "0.0.0.0", module = Application::module)
        server?.start(wait = true) ?: throw Exception("Server is null")
    }

    CoroutineScope(Dispatchers.Default).launch {

        while(true) {
            val command = readln()
            when(command) {
                "register" -> {
                    when(val result = RegisterController.performRegister()) {
                        is Result.Success -> println("The authentication key has been successfully created ${result.data}")
                        is Result.Error -> println("Registration error - ${result.error.exception.message}")
                    }

                }
                "tokens" -> {
                    when(val result = Tokens.fetchAll()) {
                        is Result.Success -> {
                            println("List of tokens:")
                            result.data.forEach(::println)
                        }
                        is Result.Error -> {
                            if (result.error is DataError.TokenError.TokenDoesNotExist) {
                                println("Registration error - ${result.error}")
                            }
                        }
                    }
                }
                "haha" -> {
                    val a = OffsetDateTime.now(ZoneOffset.UTC)
                    val b = a.withOffsetSameInstant(ZoneId.systemDefault().rules.getOffset(Instant.now()))
                    println(a)
                    println(b)
                }
                "lol" -> println("lol)")
                "close" -> server?.stop()
                else -> println("Command has not been recognized")
            }
        }

    }

    serverJob.join()
}

fun Application.module() {
    configureLoginRouting()
    configureCallTasksRouting()
    configureMessageTemplatesRouting()
    configureSerialization()
}
