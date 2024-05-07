package com.database.tokens

import com.utils.DataError
import com.utils.Result
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object Tokens : Table() {

    private val id = long("id")
    private val token = varchar("token", 36)

    fun insert(tokenDTO: TokenDTO) {
        transaction {
            Tokens.insert {
                it[token] = tokenDTO.token
            }
        }
    }

    fun fetch(token: String): Result<TokenDTO, DataError.TokenError.TokenDoesNotExist> {
        return try {
            transaction{
                val tokenModel = Tokens.selectAll().where { Tokens.token.eq(token) }.single()
                val tokenDTO = TokenDTO(
                    autoIncId = tokenModel[Tokens.id],
                    token = tokenModel[Tokens.token],
                )
                Result.Success(tokenDTO)
            }
        } catch (e: NoSuchElementException) {
            Result.Error(DataError.TokenError.TokenDoesNotExist)
        }
    }

    fun fetchAll(): Result<List<TokenDTO>, DataError.TokenError> {
        return try {
            transaction {
                val tokenDtoList = mutableListOf<TokenDTO>()
                val tokenModel = Tokens.selectAll().toList()

                tokenModel.map {
                    tokenDtoList.add(
                        TokenDTO(
                            autoIncId = it[Tokens.id],
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

