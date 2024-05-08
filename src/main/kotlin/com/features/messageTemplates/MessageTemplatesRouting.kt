package com.features.messageTemplates

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureMessageTemplatesRouting() {
    routing {
        get("/get_message_templates_from_server") {
            val messageTemplatesController = MessageTemplatesController(call)
            messageTemplatesController.sendMessageTemplatesToClient()
        }
        post("/send_message_template_to_server"){
            val messageTemplatesController = MessageTemplatesController(call)
            messageTemplatesController.getMessageTemplateFromClient()
        }
        post("/remove_message_template_from_server"){
            val messageTemplatesController = MessageTemplatesController(call)
            messageTemplatesController.removeMessageTemplate()
        }
    }
}