package com.features.completedTasks

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureCompletedTasksRouting() {
    routing {
        get("/get_tasks_from_server") {
            val completedTasksController = CompletedTasksController(call)
            completedTasksController.sendCallTasksToClient()
        }
        post("/send_tasks_to_server"){
            val completedTasksController = CompletedTasksController(call)
            completedTasksController.getCallTasksFromClient()
        }
        post("/remove_task_from_server"){
            val completedTasksController = CompletedTasksController(call)
            completedTasksController.removeCallTask()
        }
    }
}