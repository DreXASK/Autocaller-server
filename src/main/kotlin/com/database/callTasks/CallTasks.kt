package com.database.callTasks

import com.utils.DataError
import com.utils.Result
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.javatime.timestampWithTimeZone
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

object CallTasks : LongIdTable("call_tasks") {

    private val surname = varchar("surname", 50).nullable()
    private val name = varchar("name", 50).nullable()
    private val patronymic = varchar("patronymic", 50).nullable()
    private val phoneNumber = varchar("phone_number", 15)
    private val messageText = varchar("message_text", 500)
    private val callAttempts = integer("call_attempts")
    private val nextCallDateTimeUTC = timestampWithTimeZone("next_call_date_time_utc")


    fun insert(callTaskDto: CallTaskDto): Result<Unit, DataError.CallTasksError.Insert> {
        return try {
            transaction {
                CallTasks.insert {
                    it[surname] = callTaskDto.surname
                    it[name] = callTaskDto.name
                    it[patronymic] = callTaskDto.patronymic
                    it[phoneNumber] = callTaskDto.phoneNumber
                    it[messageText] = callTaskDto.messageText
                    it[callAttempts] = callTaskDto.callAttempts
                    it[nextCallDateTimeUTC] = callTaskDto.nextCallDateTimeUTC
                }
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DataError.CallTasksError.Insert.UnknownError(e))
        }
    }

    fun insert(callTaskDtoList: List<CallTaskDto>): Result<Unit, DataError.CallTasksError.Insert> {
        return try {
            transaction {
                callTaskDtoList.map { callTaskDto ->
                    CallTasks.insert {
                        it[surname] = callTaskDto.surname
                        it[name] = callTaskDto.name
                        it[patronymic] = callTaskDto.patronymic
                        it[phoneNumber] = callTaskDto.phoneNumber
                        it[messageText] = callTaskDto.messageText
                        it[callAttempts] = callTaskDto.callAttempts
                        it[nextCallDateTimeUTC] = callTaskDto.nextCallDateTimeUTC
                    }
                }
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DataError.CallTasksError.Insert.UnknownError(e))
        }
    }

    fun fetchOldestCallTasksList(takeNumber: Int): Result<List<CallTaskDto>, DataError.CallTasksError.Fetch> {
        return try {
            val callTaskList = mutableListOf<CallTaskDto>()
            transaction {
                val callTaskModel = CallTasks.selectAll().sortedBy { nextCallDateTimeUTC }.take(takeNumber)

                callTaskModel.map {
                    callTaskList.add(
                        CallTaskDto(
                            id = it[CallTasks.id].value,
                            surname = it[surname],
                            name = it[name],
                            patronymic = it[patronymic],
                            phoneNumber = it[phoneNumber],
                            messageText = it[messageText],
                            callAttempts = it[callAttempts],
                            nextCallDateTimeUTC = it[nextCallDateTimeUTC],
                        )
                    )
                }

                Result.Success(callTaskList)
            }
        } catch (e: NoSuchElementException) {
            Result.Error(DataError.CallTasksError.Fetch.CallTasksDoesNotExist)
        }
    }

    fun fetch(id: Long): Result<CallTaskDto, DataError.CallTasksError.Fetch> {
        return try {
            transaction {
                val callTaskModel = CallTasks.selectAll().where { CallTasks.id.eq(id) }.single()
                val callTaskDto = CallTaskDto(
                    id = callTaskModel[CallTasks.id].value,
                    surname = callTaskModel[surname],
                    name = callTaskModel[name],
                    patronymic = callTaskModel[patronymic],
                    phoneNumber = callTaskModel[phoneNumber],
                    messageText = callTaskModel[messageText],
                    callAttempts = callTaskModel[callAttempts],
                    nextCallDateTimeUTC = callTaskModel[nextCallDateTimeUTC],
                )
                Result.Success(callTaskDto)
            }
        } catch (e: NoSuchElementException) {
            Result.Error(DataError.CallTasksError.Fetch.CallTasksDoesNotExist)
        }
    }

    fun fetchAll(): Result<List<CallTaskDto>, DataError.CallTasksError.Fetch> {
        return try {
            transaction {
                val callTaskList = mutableListOf<CallTaskDto>()
                val callTaskModel = CallTasks.selectAll().toList()

                callTaskModel.map {
                    callTaskList.add(
                        CallTaskDto(
                            id = it[CallTasks.id].value,
                            surname = it[surname],
                            name = it[name],
                            patronymic = it[patronymic],
                            phoneNumber = it[phoneNumber],
                            messageText = it[messageText],
                            callAttempts = it[callAttempts],
                            nextCallDateTimeUTC = it[nextCallDateTimeUTC],
                        )
                    )
                }

                Result.Success(callTaskList)
            }
        } catch (e: NoSuchElementException) {
            Result.Error(DataError.CallTasksError.Fetch.CallTasksDoesNotExist)
        }
    }

    fun update(dto: CallTaskDto): Result<Unit, DataError.CallTasksError.Update> {
        return try {
            transaction {
                CallTasks.update({ CallTasks.id.eq(dto.id) }) {
                    it[callAttempts] = dto.callAttempts
                    it[nextCallDateTimeUTC] = dto.nextCallDateTimeUTC
                }
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DataError.CallTasksError.Update.UnknownError(e))
        }
    }

    fun remove(id: Long): Result<Unit, DataError.CallTasksError.Remove> {
        return try {
            transaction {
                CallTasks.deleteWhere { CallTasks.id.eq(id) }
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DataError.CallTasksError.Remove.UnknownError(e))
        }
    }

}

