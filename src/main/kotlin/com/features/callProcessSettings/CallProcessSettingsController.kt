package com.features.callProcessSettings

import com.database.callProcessSettings.CallProcessSettings
import com.database.tokens.Tokens
import com.utils.DataError
import com.utils.Result
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

class CallProcessSettingsController(private val call: ApplicationCall) {

    suspend fun getCallProcessSettingsFromClient() {
        val receive = call.receive<GetCallProcessSettingsReceiveRemote>()

        auth(receive.token)

        val settingsDto = receive.settings
        when (val result = CallProcessSettings.writeToFile(settingsDto)) {
            is Result.Success -> call.respond(HttpStatusCode.OK)
            is Result.Error -> {
                when (result.error) {
                    is DataError.CallProcessSettingsError.WriteToFile.UnknownError ->
                        call.respond(
                            HttpStatusCode.BadRequest,
                            result.error.e.message.toString()
                        )
                }
            }
        }
    }

    suspend fun sendCallProcessSettingsToClient() {
        val receive = call.receive<SendCallProcessSettingsReceiveRemote>()

        auth(receive.token)

        when (val result = CallProcessSettings.fetchFromFile()) {
            is Result.Success -> call.respond(result.data)
            is Result.Error -> {
                when (result.error) {
                    is DataError.CallProcessSettingsError.FetchFromFile.FileNotFound ->
                        call.respond(
                            HttpStatusCode.BadRequest,
                            "File not found"
                        )
                    is DataError.CallProcessSettingsError.FetchFromFile.UnknownError -> call.respond(
                        HttpStatusCode.InternalServerError,
                        result.error.e.message.toString()
                    )
                }
            }
        }
    }


    private suspend fun auth(token: String) {
        when (val result = Tokens.fetch(token)) {
            is Result.Error -> {
                if (result.error == DataError.TokensError.TokensDoesNotExist)
                    call.respond(HttpStatusCode.BadRequest, "Invalid token")
            }
            is Result.Success -> Unit
        }
    }
}