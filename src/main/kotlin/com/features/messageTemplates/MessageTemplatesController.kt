package com.features.messageTemplates

import com.database.messageTemplates.MessageTemplates
import com.database.tokens.Tokens
import com.utils.ApiError
import com.utils.DataError
import com.utils.Result
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

class MessageTemplatesController(private val call: ApplicationCall) {

    suspend fun getMessageTemplateFromClient() {
        val receive = call.receive<GetMessageTemplateReceiveRemote>()

        auth(receive.token)

        val messageTemplateDto = receive.messageTemplate
        when (val result = MessageTemplates.insert(messageTemplateDto)) {
            is Result.Success -> call.respond(HttpStatusCode.OK)
            is Result.Error -> {
                when (result.error) {
                    is DataError.MessageTemplateError.Insert.UnknownError ->
                        call.respond(
                            HttpStatusCode.BadRequest,
                            ApiError.CallTasksError.Remote.UnknownError(result.error.e)
                        )
                }
            }
        }
    }

    suspend fun sendMessageTemplatesToClient() {
        val receive = call.receive<SendMessageTemplatesReceiveRemote>()

        auth(receive.token)

        when (val result = MessageTemplates.fetchAll()) {
            is Result.Success -> call.respond(result.data)
            is Result.Error -> {
                when (result.error) {
                    is DataError.MessageTemplateError.Fetch.MessageTemplateDoesNotExist ->
                        call.respond(HttpStatusCode.BadRequest, ApiError.CallTasksError.Remote.UnknownError(null))
                }
            }
        }
    }

    suspend fun removeMessageTemplate() {
        val receive = call.receive<RemoveMessageTemplateReceiveRemote>()

        auth(receive.token)

        when (val result = MessageTemplates.remove(receive.id)) {
            is Result.Success -> call.respond(HttpStatusCode.OK)
            is Result.Error -> {
                when (result.error) {
                    is DataError.MessageTemplateError.Remove.UnknownError -> call.respond(
                        HttpStatusCode.BadRequest,
                        ApiError.CallTasksError.Remote.UnknownError(result.error.e)
                    )
                }
            }
        }
    }

    private suspend fun auth(token: String) {
        when (val result = Tokens.fetch(token)) {
            is Result.Error -> {
                if (result.error == DataError.TokenError.TokenDoesNotExist)
                    call.respond(HttpStatusCode.BadRequest, ApiError.TokenStatusError.INVALID_TOKEN)
            }

            is Result.Success -> Unit
        }
    }
}