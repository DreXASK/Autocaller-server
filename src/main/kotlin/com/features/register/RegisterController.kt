package com.features.register

import com.database.tokens.TokenDto
import com.database.tokens.Tokens
import com.utils.DataError
import com.utils.Result
import java.util.UUID

object RegisterController {

    fun performRegister(): Result<String, DataError.TokensError.TokensCreationError> {
        return try {
            val token = UUID.randomUUID().toString()
            val tokenDTO = TokenDto(token)
            Tokens.insert(tokenDTO)
            Result.Success(token)
        } catch (e: Exception) {
            Result.Error(DataError.TokensError.TokensCreationError(e))
        }
    }

}