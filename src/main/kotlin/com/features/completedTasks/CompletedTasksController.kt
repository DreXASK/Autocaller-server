package com.features.completedTasks

import com.database.completedTasks.CompletedTasks
import com.features.auth
import com.utils.DataError
import com.utils.Result
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

class CompletedTasksController(private val call: ApplicationCall) {

    suspend fun sendCompletedTasksToClient() {
        val receive = call.receive<SendCompletedTasksReceiveRemote>()

        if (!auth(receive.token, call)) {
            return
        }

        when (val result = CompletedTasks.fetchAll()) {
            is Result.Success -> call.respond(result.data)
            is Result.Error -> {
                when (result.error) {
                    is DataError.CompletedTasksError.Fetch.CallTasksDoesNotExist ->
                        call.respond(HttpStatusCode.BadRequest, "CompletedCallTaskTable is empty")
                }
            }
        }
    }

    suspend fun getCompletedTasksFromClient() {
        val receive = call.receive<GetCompletedTasksReceiveRemote>()

        if (!auth(receive.token, call)) {
            return
        }

        val completedTaskDto = receive.completedTaskDto
        when (val result = CompletedTasks.insert(completedTaskDto)) {
            is Result.Success -> call.respond(HttpStatusCode.OK)
            is Result.Error -> {
                when (result.error) {
                    is DataError.CompletedTasksError.Insert.UnknownError ->
                        call.respond(
                            HttpStatusCode.BadRequest,
                            result.error.e.message.toString()
                        )
                }
            }
        }
    }

}