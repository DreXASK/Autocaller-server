package com.utils

sealed interface DataError: Error {
    sealed interface TokensError: DataError {
        data class TokensCreationError(val exception: Exception) : TokensError
        data object TokensDoesNotExist: TokensError
    }
    sealed interface CallTasksError: DataError {
        sealed interface Insert: CallTasksError {
            data class UnknownError(val e: Exception): Insert
        }
        sealed interface Fetch: CallTasksError {
            data object CallTasksDoesNotExist: Fetch
        }
        sealed interface Remove: CallTasksError {
            data class UnknownError(val e: Exception): Remove
        }
    }
    sealed interface CompletedTasksError: DataError {
        sealed interface Insert: CompletedTasksError {
            data class UnknownError(val e: Exception): Insert
        }
        sealed interface Fetch: CompletedTasksError {
            data object CallTasksDoesNotExist: Fetch
        }
    }
    sealed interface MessageTemplatesError: DataError {
        sealed interface Insert: MessageTemplatesError {
            data class UnknownError(val e: Exception): Insert
        }
        sealed interface Fetch: MessageTemplatesError {
            data object MessageTemplatesDoesNotExist: Fetch
        }
        sealed interface Remove: MessageTemplatesError {
            data class UnknownError(val e: Exception): Remove
        }
    }
}