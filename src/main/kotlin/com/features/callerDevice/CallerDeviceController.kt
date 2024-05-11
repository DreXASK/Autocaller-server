package com.features.callerDevice

import com.database.callProcessSettings.CallProcessSettings
import com.database.callTasks.CallTasks
import com.database.callTasksInWork.CallTaskInWorkDto
import com.database.callTasksInWork.CallTasksInWork
import com.database.completedTasks.CompletedTaskDto
import com.database.completedTasks.CompletedTasks
import com.features.auth
import com.features.checkIsFetchDatetimeOfCallTaskInWorkOutdated
import com.utils.Result
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.OffsetDateTime
import java.time.ZoneOffset

class CallerDeviceController(private val call: ApplicationCall) {

    suspend fun sendOldestCallTaskToCallerDevice() {
        val receive = call.receive<SendOlderCallTaskReceiveRemote>()

        if (!auth(receive.token, call)) {
            println("unauthorized")
            return
        }

        val oldestTaskResult = CallTasks.fetch25Oldest()
        if (oldestTaskResult is Result.Error) {
            call.respond(HttpStatusCode.NoContent)
            return
        }


        (oldestTaskResult as Result.Success).data.map {
            if (it.id == null)
                throw Exception("id == null")

            println(it.id)

            when (val inWorkFetchResult = CallTasksInWork.fetch(it.id)) {
                is Result.Success -> {

                    if (!checkIsFetchDatetimeOfCallTaskInWorkOutdated(inWorkFetchResult.data.dateTimeOfFetchUTC))
                        return@map

                    CallTasksInWork.update(
                        inWorkFetchResult.data.copy(
                            dateTimeOfFetchUTC = OffsetDateTime.now(
                                ZoneOffset.UTC
                            )
                        )
                    )
                    call.respond(it)
                    return
                }

                is Result.Error -> {
                    CallTasksInWork.insert(
                        CallTaskInWorkDto(
                            id = it.id,
                            dateTimeOfFetchUTC = OffsetDateTime.now(ZoneOffset.UTC)
                        )
                    )
                    call.respond(it)
                    return
                }
            }
        }

        call.respond(HttpStatusCode.InternalServerError)
    }

    suspend fun getResultFromCallerDevice() {
        val receive = call.receive<ResultCallTaskReceiveRemote>()

        auth(receive.token, call)

        val callTaskFetchResult = CallTasks.fetch(receive.id)
        if (callTaskFetchResult is Result.Error)
            throw Exception("getResultFromCallerDevice error - ${callTaskFetchResult.error}")

        val settingsFetchResult = CallProcessSettings.fetchFromFile()
        if (settingsFetchResult is Result.Error)
            throw Exception("getResultFromCallerDevice error - ${settingsFetchResult.error}")

        if ((callTaskFetchResult as Result.Success).data.id == null)
            throw Exception("getResultFromCallerDevice error - CallTask's id is null")


        if (receive.isSuccess) {
            transaction {
                CallTasksInWork.remove(callTaskFetchResult.data.id!!)
                CallTasks.remove(callTaskFetchResult.data.id)
                CompletedTasks.insert(
                    CompletedTaskDto(
                        id = callTaskFetchResult.data.id,
                        surname = callTaskFetchResult.data.surname,
                        name = callTaskFetchResult.data.name,
                        patronymic = callTaskFetchResult.data.patronymic,
                        phoneNumber = callTaskFetchResult.data.phoneNumber,
                        messageText = callTaskFetchResult.data.messageText,
                        callAttempts = callTaskFetchResult.data.callAttempts + 1,
                        informDateTime = OffsetDateTime.now(ZoneOffset.UTC),
                        isSmsUsed = false
                    )
                )
            }
        } else {
            if (callTaskFetchResult.data.callAttempts == 2) {
                transaction {
                    CallTasksInWork.remove(callTaskFetchResult.data.id!!)
                    CallTasks.remove(callTaskFetchResult.data.id)
                    CompletedTasks.insert(
                        CompletedTaskDto(
                            id = callTaskFetchResult.data.id,
                            surname = callTaskFetchResult.data.surname,
                            name = callTaskFetchResult.data.name,
                            patronymic = callTaskFetchResult.data.patronymic,
                            phoneNumber = callTaskFetchResult.data.phoneNumber,
                            messageText = callTaskFetchResult.data.messageText,
                            callAttempts = callTaskFetchResult.data.callAttempts + 1,
                            informDateTime = OffsetDateTime.now(ZoneOffset.UTC),
                            isSmsUsed = true
                        )
                    )
                }
            } else {
                transaction {
                    CallTasksInWork.remove(callTaskFetchResult.data.id!!)
                    CallTasks.update(
                        callTaskFetchResult.data.copy(
                            callAttempts = callTaskFetchResult.data.callAttempts + 1,
                            nextCallDateTimeUTC = OffsetDateTime.now(ZoneOffset.UTC).plusHours(6)
                        )
                    )
                }
            }
        }
        call.respond(HttpStatusCode.OK)
    }

}