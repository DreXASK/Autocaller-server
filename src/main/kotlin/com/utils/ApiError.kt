package com.utils

import kotlinx.serialization.Serializable

//sealed interface ApiError: Error {
//    enum class TokenError: ApiError {
//        INVALID_CONNECTION_KEY
//    }
//    enum class TokenStatusError: ApiError {
//        INVALID_TOKEN
//    }
//    sealed interface CallTasksError: ApiError {
//        sealed interface Remote: CallTasksError {
//            data class UnknownError(val exception: Exception?): Remote
//        }
//    }
//    sealed interface MessageTemplatesError: ApiError {
//        sealed interface Remote: MessageTemplatesError {
//            data class UnknownError(val exception: Exception?): Remote
//        }
//    }
//    sealed interface CompletedTasksError: ApiError {
//        sealed interface Remote: CompletedTasksError {
//            data class UnknownError(val exception: Exception?): Remote
//        }
//    }
//    sealed interface CallProcessSettings: ApiError {
//        sealed interface Remote: CallProcessSettings {
//           data class UnknownError(@Serializable(with = ExceptionSerializer::class) val exception: Exception?): Remote
//        }
//    }
//    enum class Network: ApiError {
//        CONNECTION_REFUSED,
//        REQUEST_TIMEOUT
//    }
//    data object UnknownError : ApiError
//}