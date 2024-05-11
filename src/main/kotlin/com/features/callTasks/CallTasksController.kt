package com.features.callTasks

import com.database.callTasks.CallTasks
import com.database.callTasksInWork.CallTaskInWorkDto
import com.database.callTasksInWork.CallTasksInWork
import com.database.tokens.Tokens
import com.features.auth
import com.features.checkIsFetchDatetimeOfCallTaskInWorkOutdated
import com.utils.DataError
import com.utils.Result
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.OffsetDateTime
import java.time.ZoneOffset

class CallTasksController(private val call: ApplicationCall) {

    suspend fun getCallTasksFromClient() {
        val receive = call.receive<GetCallTasksReceiveRemote>()

        if (!auth(receive.token, call)) {
            return
        }

        val callTaskDtoList = receive.list
        when (val result = CallTasks.insert(callTaskDtoList)) {
            is Result.Success -> call.respond(HttpStatusCode.OK)
            is Result.Error -> {
                when (result.error) {
                    is DataError.CallTasksError.Insert.UnknownError ->
                        call.respond(
                            HttpStatusCode.BadRequest,
                            result.error.e.message.toString()
                        )
                }
            }
        }
    }

    suspend fun sendCallTasksToClient() {
        val receive = call.receive<SendCallTasksReceiveRemote>()

        if (!auth(receive.token, call)) {
            return
        }

        when (val result = CallTasks.fetchAll()) {
            is Result.Success -> call.respond(result.data)
            is Result.Error -> {
                when (result.error) {
                    is DataError.CallTasksError.Fetch.CallTasksDoesNotExist ->
                        call.respond(HttpStatusCode.BadRequest, "CallTasksTable is empty")
                }
            }
        }
    }

    suspend fun removeCallTask() {
        val receive = call.receive<RemoveCallTaskReceiveRemote>()

        if (!auth(receive.token, call)) {
            return
        }

        val inWorkFetchResult = CallTasksInWork.fetch(receive.id)
        if (inWorkFetchResult is Result.Success) {
            if (checkIsFetchDatetimeOfCallTaskInWorkOutdated(inWorkFetchResult.data.dateTimeOfFetchUTC)) {
                CallTasksInWork.remove(inWorkFetchResult.data.id)
            } else {
                call.respond(HttpStatusCode.BadRequest, "The CallTask is currently in an executing state")
            }
        }

        when (val result = CallTasks.remove(receive.id)) {
            is Result.Success -> call.respond(HttpStatusCode.OK)
            is Result.Error -> {
                when (result.error) {
                    is DataError.CallTasksError.Remove.UnknownError -> call.respond(
                        HttpStatusCode.BadRequest,
                        result.error.e.message.toString()
                    )
                }
            }
        }
    }
}