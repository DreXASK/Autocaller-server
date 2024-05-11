package com.features.callerDevice

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureCallerDeviceRouting() {
    routing {
        post("/get_another_call_task") {
            val callTasksController = CallerDeviceController(call)
            callTasksController.sendOldestCallTaskToCallerDevice()
        }
        post("/send_call_result_to_server"){
            val callTasksController = CallerDeviceController(call)
            callTasksController.getResultFromCallerDevice()
        }
    }
}