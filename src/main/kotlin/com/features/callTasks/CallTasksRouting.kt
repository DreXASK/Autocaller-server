package com.features.callTasks

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureCallTasksRouting() {
    routing {
        get("/get_tasks_from_server") {
            val callTasksController = CallTasksController(call)
            callTasksController.sendCallTasksToClient()
        }
        post("/send_tasks_to_server"){
            val callTasksController = CallTasksController(call)
            callTasksController.getCallTasksFromClient()
        }
        post("/remove_task_from_server"){
            val callTasksController = CallTasksController(call)
            callTasksController.removeCallTask()
        }
    }
}