package com.utils

sealed interface DataError: Error {
    sealed interface TokenError: DataError {
        data class TokenCreationError(val exception: Exception) : TokenError
        data object TokenDoesNotExist: TokenError
    }
    sealed interface CallTaskError: DataError {
        sealed interface Insert: CallTaskError {
            data class UnknownError(val e: Exception): Insert
        }
        sealed interface Fetch: CallTaskError {
            data object CallTaskDoesNotExist: Fetch
        }
        sealed interface Remove: CallTaskError {
            data class UnknownError(val e: Exception): Remove
        }
    }
}