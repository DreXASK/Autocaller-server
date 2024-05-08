package com.utils

sealed interface ApiError: Error {
    enum class TokenError: ApiError {
        INVALID_CONNECTION_KEY
    }
    enum class TokenStatusError: ApiError {
        INVALID_TOKEN
    }
    sealed interface CallTasksError: ApiError {
        sealed interface Remote: CallTasksError {
            data class UnknownError(val exception: Exception?): Remote
        }
    }
    enum class Network: ApiError {
        CONNECTION_REFUSED,
        REQUEST_TIMEOUT
    }
    data object UnknownError : ApiError
}