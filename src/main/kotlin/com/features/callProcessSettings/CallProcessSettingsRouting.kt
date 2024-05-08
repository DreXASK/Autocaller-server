package com.features.callProcessSettings

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureCallProcessSettingsRouting() {
    routing {
        get("/get_call_process_settings_from_server") {
            val callTasksController = CallProcessSettingsController(call)
            callTasksController.sendCallProcessSettingsToClient()
        }
        post("/send_call_process_settings_to_server"){
            val callTasksController = CallProcessSettingsController(call)
            callTasksController.getCallProcessSettingsFromClient()
        }
    }
}