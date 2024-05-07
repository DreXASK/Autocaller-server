package com.utils

sealed interface DataError: Error {
    sealed interface TokenError: DataError {
        data class TokenCreationError(val exception: Exception) : TokenError
        data object TokenDoesNotExist: TokenError
    }
}