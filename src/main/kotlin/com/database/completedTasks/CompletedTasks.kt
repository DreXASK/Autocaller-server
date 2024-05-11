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

object CompletedTasks: LongIdTable("completed_tasks") {

    private val surname = varchar("surname", 50).nullable()
    private val name = varchar("name", 50).nullable()
    private val patronymic = varchar("patronymic", 50).nullable()
    private val phoneNumber = varchar("phone_number", 15)
    private val messageText = varchar("message_text", 500)
    private val callAttempts = integer("call_attempts")
    private val isSmsUsed = bool("is_sms_used")
    private val informDateTime = timestampWithTimeZone("inform_date_time")

    fun insert(completedTaskDto: CompletedTaskDto): Result<Unit, DataError.CompletedTasksError.Insert> {
        return try {
            transaction {
                CompletedTasks.insert {
                    it[surname] = completedTaskDto.surname
                    it[name] = completedTaskDto.name
                    it[patronymic] = completedTaskDto.patronymic
                    it[phoneNumber] = completedTaskDto.phoneNumber
                    it[messageText] = completedTaskDto.messageText
                    it[callAttempts] = completedTaskDto.callAttempts
                    it[isSmsUsed] = completedTaskDto.isSmsUsed
                    it[informDateTime] = completedTaskDto.informDateTimeUTC
                }
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DataError.CompletedTasksError.Insert.UnknownError(e))
        }
    }

    fun insert(completedTaskDtoList: List<CompletedTaskDto>): Result<Unit, DataError.CompletedTasksError.Insert> {
        return try {
            transaction {
                completedTaskDtoList.map { completedTaskDto ->
                    CompletedTasks.insert {
                        it[surname] = completedTaskDto.surname
                        it[name] = completedTaskDto.name
                        it[patronymic] = completedTaskDto.patronymic
                        it[phoneNumber] = completedTaskDto.phoneNumber
                        it[messageText] = completedTaskDto.messageText
                        it[callAttempts] = completedTaskDto.callAttempts
                        it[isSmsUsed] = completedTaskDto.isSmsUsed
                        it[informDateTime] = completedTaskDto.informDateTimeUTC
                    }
                }
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DataError.CompletedTasksError.Insert.UnknownError(e))
        }
    }

    fun fetch(id: Long): Result<CompletedTaskDto, DataError.CompletedTasksError.Fetch> {
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
                    isSmsUsed = callTaskModel[isSmsUsed],
                    informDateTimeUTC = callTaskModel[informDateTime]
                )
                Result.Success(completedTaskDto)
            }
        } catch (e: NoSuchElementException) {
            Result.Error(DataError.CompletedTasksError.Fetch.CallTasksDoesNotExist)
        }
    }

    fun fetchAll(): Result<List<CompletedTaskDto>, DataError.CompletedTasksError.Fetch> {
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
                            isSmsUsed = it[isSmsUsed],
                            informDateTimeUTC = it[informDateTime]
                        )
                    )
                }

                Result.Success(callTaskList)
            }
        } catch (e: NoSuchElementException) {
            Result.Error(DataError.CompletedTasksError.Fetch.CallTasksDoesNotExist)
        }
    }

    fun remove(id: Long): Result<Unit, DataError.CallTasksError.Remove> {
        return try {
            transaction {
                CompletedTasks.deleteWhere { CompletedTasks.id.eq(id) }
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DataError.CallTasksError.Remove.UnknownError(e))
        }
    }

}

