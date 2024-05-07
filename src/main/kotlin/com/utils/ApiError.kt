package com.utils

sealed interface ApiError: Error {
    enum class TokenError: ApiError {
        INVALID_CONNECTION_KEY
    }
    enum class TokenStatusError: ApiError {
        INVALID_TOKEN
    }
    enum class Network: ApiError {
        CONNECTION_REFUSED,
        REQUEST_TIMEOUT
    }
    data object UnknownError : ApiError
}