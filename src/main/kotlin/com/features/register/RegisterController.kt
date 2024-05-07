package com.features.register

import com.database.tokens.TokenDto
import com.database.tokens.Tokens
import com.utils.DataError
import com.utils.Result
import java.util.UUID

object RegisterController {

    fun performRegister(): Result<String, DataError.TokenError.TokenCreationError> {
        return try {
            val token = UUID.randomUUID().toString()
            val tokenDTO = TokenDto(autoIncId = null, token = token)
            Tokens.insert(tokenDTO)
            Result.Success(token)
        } catch (e: Exception) {
            Result.Error(DataError.TokenError.TokenCreationError(e))
        }
    }

}