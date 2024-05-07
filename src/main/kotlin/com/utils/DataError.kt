package com.utils

sealed interface DataError: Error {
    enum class TokenError: DataError {
        CONNECTION_SECRET_KEY_DOES_NOT_EXIST,
    }
    enum class TokenStatusError: DataError {
        TOKEN_DOES_NOT_EXIST,
    }
}