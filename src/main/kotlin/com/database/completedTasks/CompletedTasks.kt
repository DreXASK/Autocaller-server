package com.database.completedTasks

import com.utils.DataError
import com.utils.Result
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.javatime.timestampWithTimeZone
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object CompletedTasks: LongIdTable("completed_task") {

    private val surname = varchar("surname", 50).nullable()
    private val name = varchar("name", 50).nullable()
    private val patronymic = varchar("patronymic", 50).nullable()
    private val phoneNumber = varchar("phone_number", 50)
    private val messageText = varchar("message_text", 500)
    private val callAttempts = integer("call_attempts")
    private val nextCallDateAndTimeUTC = timestampWithTimeZone("next_call_date_and_time_utc")


    fun insert(completedTaskDto: CompletedTaskDto): Result<Unit, DataError.CallTaskError.Insert> {
        return try {
            transaction {
                CompletedTasks.insert {
                    it[surname] = completedTaskDto.surname
                    it[name] = completedTaskDto.name
                    it[patronymic] = completedTaskDto.patronymic
                    it[phoneNumber] = completedTaskDto.phoneNumber
                    it[messageText] = completedTaskDto.messageText
                    it[callAttempts] = completedTaskDto.callAttempts
                    it[nextCallDateAndTimeUTC] = completedTaskDto.nextCallDateAndTimeUTC
                }
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DataError.CallTaskError.Insert.UnknownError(e))
        }
    }

    fun insert(completedTaskDtoList: List<CompletedTaskDto>): Result<Unit, DataError.CallTaskError.Insert> {
        return try {
            transaction {
                completedTaskDtoList.map { callTaskDto ->
                    CompletedTasks.insert {
                        it[surname] = callTaskDto.surname
                        it[name] = callTaskDto.name
                        it[patronymic] = callTaskDto.patronymic
                        it[phoneNumber] = callTaskDto.phoneNumber
                        it[messageText] = callTaskDto.messageText
                        it[callAttempts] = callTaskDto.callAttempts
                        it[nextCallDateAndTimeUTC] = callTaskDto.nextCallDateAndTimeUTC
                    }
                }
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DataError.CallTaskError.Insert.UnknownError(e))
        }
    }

    fun fetch(): Result<CompletedTaskDto, DataError.CallTaskError.Fetch> {
        return try {
            transaction {
                val callTaskModel = CompletedTasks.selectAll().sortedBy { nextCallDateAndTimeUTC }.single()
                val completedTaskDto = CompletedTaskDto(
                    id = callTaskModel[CompletedTasks.id].value,
                    surname = callTaskModel[surname],
                    name = callTaskModel[name],
                    patronymic = callTaskModel[patronymic],
                    phoneNumber = callTaskModel[phoneNumber],
                    messageText = callTaskModel[messageText],
                    callAttempts = callTaskModel[callAttempts],
                    nextCallDateAndTimeUTC = callTaskModel[nextCallDateAndTimeUTC],
                )
                Result.Success(completedTaskDto)
            }
        } catch (e: NoSuchElementException) {
            Result.Error(DataError.CallTaskError.Fetch.CallTaskDoesNotExist)
        }
    }

    fun fetch(id: Long): Result<CompletedTaskDto, DataError.CallTaskError.Fetch> {
        return try {
            transaction {
                val callTaskModel = CompletedTasks.selectAll().where { CompletedTasks.id.eq(id) }.single()
                val completedTaskDto = CompletedTaskDto(
                    id = callTaskModel[CompletedTasks.id].value,
                    surname = callTaskModel[surname],
                    name = callTaskModel[name],
                    patronymic = callTaskModel[patronymic],
                    phoneNumber = callTaskModel[phoneNumber],
                    messageText = callTaskModel[messageText],
                    callAttempts = callTaskModel[callAttempts],
                    nextCallDateAndTimeUTC = callTaskModel[nextCallDateAndTimeUTC],
                )
                Result.Success(completedTaskDto)
            }
        } catch (e: NoSuchElementException) {
            Result.Error(DataError.CallTaskError.Fetch.CallTaskDoesNotExist)
        }
    }

    fun fetchAll(): Result<List<CompletedTaskDto>, DataError.CallTaskError.Fetch> {
        return try {
            transaction {
                val callTaskList = mutableListOf<CompletedTaskDto>()
                val callTaskModel = CompletedTasks.selectAll().toList()

                callTaskModel.map {
                    callTaskList.add(
                        CompletedTaskDto(
                            id = it[CompletedTasks.id].value,
                            surname = it[surname],
                            name = it[name],
                            patronymic = it[patronymic],
                            phoneNumber = it[phoneNumber],
                            messageText = it[messageText],
                            callAttempts = it[callAttempts],
                            nextCallDateAndTimeUTC = it[nextCallDateAndTimeUTC],
                        )
                    )
                }

                Result.Success(callTaskList)
            }
        } catch (e: NoSuchElementException) {
            Result.Error(DataError.CallTaskError.Fetch.CallTaskDoesNotExist)
        }
    }

    fun remove(id: Long): Result<Unit, DataError.CallTaskError.Remove> {
        return try {
            transaction {
                CompletedTasks.deleteWhere { CompletedTasks.id.eq(id) }
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DataError.CallTaskError.Remove.UnknownError(e))
        }
    }

}

