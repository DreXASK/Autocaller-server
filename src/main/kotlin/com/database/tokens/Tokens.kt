package com.database.tokens

import com.utils.DataError
import com.utils.Result
import com.utils.TokenStatus
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object Tokens : Table() {

    private val id = long("id")
    private val token = varchar("token", 36)
    private val tokenStatus = enumerationByName("token_status", 50, TokenStatus::class)

    fun insert(tokenDTO: TokenDTO) {
        transaction {
            Tokens.insert {
                //it[id] = tokenDTO.rowId
                it[token] = tokenDTO.token
                it[tokenStatus] = tokenDTO.tokenStatus
            }
        }
    }

    fun fetch(token: String): Result<TokenDTO, DataError.TokenStatusError> {
        return try {
            transaction{
                val tokenModel = Tokens.selectAll().where { Tokens.token.eq(token) }.single()
                val tokenDTO = TokenDTO(
                    rowId = tokenModel[Tokens.id],
                    token = tokenModel[Tokens.token],
                    tokenStatus = tokenModel[tokenStatus]
                )
                Result.Success(tokenDTO)
            }
        } catch (e: NoSuchElementException) {
            Result.Error(DataError.TokenStatusError.TOKEN_DOES_NOT_EXIST)
        }
    }

    fun fetchAll(): Result<List<TokenDTO>, DataError.TokenStatusError> {
        return try {
            transaction {
                val tokenDtoList = mutableListOf<TokenDTO>()
                val tokenModel = Tokens.selectAll().toList()

                tokenModel.map {
                    tokenDtoList.add(
                        TokenDTO(
                            rowId = it[Tokens.id],
                            token = it[token],
                            tokenStatus = it[tokenStatus]
                        )
                    )
                }

                Result.Success(tokenDtoList)
            }
        } catch (e: NoSuchElementException) {
            Result.Error(DataError.TokenStatusError.TOKEN_DOES_NOT_EXIST)
        }
    }
}

