package com.features.messageTemplates

import com.database.messageTemplates.MessageTemplates
import com.database.tokens.Tokens
import com.features.auth
import com.utils.DataError
import com.utils.Result
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

class MessageTemplatesController(private val call: ApplicationCall) {

    suspend fun getMessageTemplateFromClient() {
        val receive = call.receive<GetMessageTemplateReceiveRemote>()

        if (!auth(receive.token, call)) {
            return
        }

        val messageTemplateDto = receive.messageTemplate
        when (val result = MessageTemplates.insert(messageTemplateDto)) {
            is Result.Success -> call.respond(HttpStatusCode.OK)
            is Result.Error -> {
                when (result.error) {
                    is DataError.MessageTemplatesError.Insert.UnknownError ->
                        call.respond(
                            HttpStatusCode.BadRequest,
                            result.error.e.message.toString()
                        )
                }
            }
        }
    }

    suspend fun sendMessageTemplatesToClient() {
        val receive = call.receive<SendMessageTemplatesReceiveRemote>()

        if (!auth(receive.token, call)) {
            return
        }

        when (val result = MessageTemplates.fetchAll()) {
            is Result.Success -> call.respond(result.data)
            is Result.Error -> {
                when (result.error) {
                    is DataError.MessageTemplatesError.Fetch.MessageTemplatesDoesNotExist ->
                        call.respond(HttpStatusCode.BadRequest, "MessageTemplatesTable is empty")
                }
            }
        }
    }

    suspend fun removeMessageTemplate() {
        val receive = call.receive<RemoveMessageTemplateReceiveRemote>()

        if (!auth(receive.token, call)) {
            return
        }

        when (val result = MessageTemplates.remove(receive.id)) {
            is Result.Success -> call.respond(HttpStatusCode.OK)
            is Result.Error -> {
                when (result.error) {
                    is DataError.MessageTemplatesError.Remove.UnknownError -> call.respond(
                        HttpStatusCode.BadRequest,
                        result.error.e.message.toString()
                    )
                }
            }
        }
    }

}