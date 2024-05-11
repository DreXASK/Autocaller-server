package com.database.callTasksInWork

import com.database.callTasks.CallTasks
import com.utils.DataError
import com.utils.Result
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.javatime.timestampWithTimeZone
import org.jetbrains.exposed.sql.transactions.transaction

object CallTasksInWork: Table("call_tasks_in_work") {

    private val id = reference("id", CallTasks)
    private val dateTimeOfFetchUTC = timestampWithTimeZone("date_time_of_fetch_utc")

    fun insert(callTaskInWorkDto: CallTaskInWorkDto): Result<Unit, DataError.CallTasksInWorkError.Insert> {
        return try {
            transaction {

                val callTaskId = CallTasks.selectAll().where { CallTasks.id.eq(callTaskInWorkDto.id) }.single()[CallTasks.id]

                CallTasksInWork.insert {
                    it[id] = callTaskId
                    it[dateTimeOfFetchUTC] = callTaskInWorkDto.dateTimeOfFetchUTC
                }
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DataError.CallTasksInWorkError.Insert.UnknownError(e))
        }
    }

    fun fetch(id: Long): Result<CallTaskInWorkDto, DataError.CallTasksInWorkError.Fetch> {
        return try {
            transaction {
                val callTaskInWorkModel = CallTasksInWork.selectAll().where { CallTasksInWork.id.eq(id) }.single()
                val callTaskInWorkDto = CallTaskInWorkDto(
                    id = callTaskInWorkModel[CallTasksInWork.id].value,
                    dateTimeOfFetchUTC = callTaskInWorkModel[dateTimeOfFetchUTC]
                )
                Result.Success(callTaskInWorkDto)
            }
        } catch (e: NoSuchElementException) {
            Result.Error(DataError.CallTasksInWorkError.Fetch.CallTasksDoesNotExist)
        }
    }

    fun fetchAll(): Result<List<CallTaskInWorkDto>, DataError.CallTasksInWorkError.Fetch> {
        return try {
            transaction {
                val callTaskInWorkList = mutableListOf<CallTaskInWorkDto>()
                val callTaskInWorkModel = CallTasksInWork.selectAll().toList()

                callTaskInWorkModel.map {
                    callTaskInWorkList.add(
                        CallTaskInWorkDto(
                            id = it[CallTasksInWork.id].value,
                            dateTimeOfFetchUTC = it[dateTimeOfFetchUTC]
                        )
                    )
                }

                Result.Success(callTaskInWorkList)
            }
        } catch (e: NoSuchElementException) {
            Result.Error(DataError.CallTasksInWorkError.Fetch.CallTasksDoesNotExist)
        }
    }

    fun update(dto: CallTaskInWorkDto): Result<Unit, DataError.CallTasksInWorkError.Update> {
        return try {
            transaction {
                CallTasksInWork.update({ CallTasksInWork.id.eq(dto.id) }) {
                    it[dateTimeOfFetchUTC] = dto.dateTimeOfFetchUTC
                }
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DataError.CallTasksInWorkError.Update.UnknownError(e))
        }
    }

    fun remove(id: Long): Result<Unit, DataError.CallTasksInWorkError.Remove> {
        return try {
            transaction {
                CallTasksInWork.deleteWhere { CallTasksInWork.id.eq(id) }
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DataError.CallTasksInWorkError.Remove.UnknownError(e))
        }
    }

}

