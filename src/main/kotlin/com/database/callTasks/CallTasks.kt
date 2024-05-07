package com.database.callTasks

import com.utils.DataError
import com.utils.Result
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object CallTasks : LongIdTable() {

    val surname = varchar("surname", 50)
    val name = varchar("name",50)
    val patronymic = varchar("patronymic", 50)
    val phoneNumber = varchar("phone_number", 50)
    val messageText = varchar("message", 500)
    val callAttempts = integer("call_attempts")
    //val nextCallDateAndTimeUTC: String = datetime

//    private val id = long("id")
//    private val token = varchar("token", 36)
//
//    fun insert(tokenDTO: TokenDto) {
//        transaction {
//            CallTasks.insert {
//                it[token] = tokenDTO.token
//            }
//        }
//    }
//
//    fun fetch(token: String): Result<TokenDto, DataError.TokenError.TokenDoesNotExist> {
//        return try {
//            transaction{
//                val tokenModel = CallTasks.selectAll().where { CallTasks.token.eq(token) }.single()
//                val tokenDTO = TokenDto(
//                    autoIncId = tokenModel[CallTasks.id],
//                    token = tokenModel[CallTasks.token],
//                )
//                Result.Success(tokenDTO)
//            }
//        } catch (e: NoSuchElementException) {
//            Result.Error(DataError.TokenError.TokenDoesNotExist)
//        }
//    }
//
//    fun fetchAll(): Result<List<TokenDto>, DataError.TokenError> {
//        return try {
//            transaction {
//                val tokenDtoList = mutableListOf<TokenDto>()
//                val tokenModel = CallTasks.selectAll().toList()
//
//                tokenModel.map {
//                    tokenDtoList.add(
//                        TokenDto(
//                            autoIncId = it[CallTasks.id],
//                            token = it[token],
//                        )
//                    )
//                }
//
//                Result.Success(tokenDtoList)
//            }
//        } catch (e: NoSuchElementException) {
//            Result.Error(DataError.TokenError.TokenDoesNotExist)
//        }
//    }
}

