package com.database.tokens

import com.utils.DataError
import com.utils.Result
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.autoIncColumnType
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object Tokens : LongIdTable() {

    private val token = varchar("token", 36)

    fun insert(tokenDTO: TokenDto) {
        transaction {
            Tokens.insert {
                it[token] = tokenDTO.token
            }
        }
    }

    fun fetch(token: String): Result<TokenDto, DataError.TokenError.TokenDoesNotExist> {
        return try {
            transaction{
                val tokenModel = Tokens.selectAll().where { Tokens.token.eq(token) }.single()
                val tokenDTO = TokenDto(
                    autoIncId = tokenModel[Tokens.id].value,
                    token = tokenModel[Tokens.token],
                )
                Result.Success(tokenDTO)
            }
        } catch (e: NoSuchElementException) {
            Result.Error(DataError.TokenError.TokenDoesNotExist)
        }
    }

    fun fetchAll(): Result<List<TokenDto>, DataError.TokenError> {
        return try {
            transaction {
                val tokenDtoList = mutableListOf<TokenDto>()
                val tokenModel = Tokens.selectAll().toList()

                tokenModel.map {
                    tokenDtoList.add(
                        TokenDto(
                            autoIncId = it[Tokens.id].value,
                            token = it[token],
                        )
                    )
                }

                Result.Success(tokenDtoList)
            }
        } catch (e: NoSuchElementException) {
            Result.Error(DataError.TokenError.TokenDoesNotExist)
        }
    }
}

