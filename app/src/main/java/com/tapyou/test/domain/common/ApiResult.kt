package com.tapyou.test.domain.common

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

sealed interface ApiResult<T> {

    /**
     * Represents a network result that successfully received a response containing body data.
     */
    class Success<T>(val data: T) : ApiResult<T>

    /**
     * Represents a network result that was successfully completed without containing body data.
     */
    class SuccessNoResponse<T> : ApiResult<T>

    /**
     * Represents a network result that successfully received a response containing an error message.
     */
    class Error<T>(val message: UiText, val code: Int? = null, val messageBody: String? = null) :
        ApiResult<T>


    @OptIn(ExperimentalContracts::class)
    companion object {

        inline fun <T> ApiResult<T>.onSuccess(action: (value: T) -> Unit): ApiResult<T> {
            contract {
                callsInPlace(action, InvocationKind.AT_MOST_ONCE)
            }
            if (this is Success) action(data)
            return this
        }

        inline fun <T, R> ApiResult<T>.convertOnSuccess(action: (value: T) -> R): ApiResult<R> {
            contract {
                callsInPlace(action, InvocationKind.AT_MOST_ONCE)
            }
            return when (this) {
                is Error -> Error(message, code, messageBody)
                is Success -> Success(action(data))
                is SuccessNoResponse -> SuccessNoResponse()
            }
        }

        inline fun <T> ApiResult<T>.onSuccessNoResponse(action: () -> Unit): ApiResult<T> {
            contract {
                callsInPlace(action, InvocationKind.AT_MOST_ONCE)
            }
            if (this is SuccessNoResponse) action()
            return this
        }

        @JvmName("functionToConvertWithoutParsing")
        inline fun <T> ApiResult<T>.onError(action: (message: UiText, code: Int?, messageBody: String?) -> Unit): ApiResult<T> {
            contract {
                callsInPlace(action, InvocationKind.AT_MOST_ONCE)
            }
            if (this is Error) action(message, code, messageBody)
            return this
        }

        @OptIn(ExperimentalSerializationApi::class)
        @JvmName("functionToConvertWithParsing")
        inline fun <reified E : Any, T> ApiResult<T>.onError(action: (message: UiText, code: Int?, parsedError: E?) -> Unit): ApiResult<T> {
            contract {
                callsInPlace(action, InvocationKind.AT_MOST_ONCE)
            }

            if (this is Error) {
                val code = code
                val message = message
                val parsedError: E? = try {
                    messageBody?.let {
                        val json = Json {
                            ignoreUnknownKeys = true
                            explicitNulls = false
                        }
                        json.decodeFromString<E>(it)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }

                action(message, code, parsedError)
            }
            return this
        }
    }
}


fun <T> ApiResult<T>.toUiState(): ResourceUiState<T> {
    return when (this) {
        is ApiResult.Success -> ResourceUiState.Success(data)
        is ApiResult.Error -> ResourceUiState.Error(message)
        is ApiResult.SuccessNoResponse -> ResourceUiState.SuccessNoResponse
    }
}

fun <T> ApiResult<T>.toContentUiState(): ContentUiState<T> {
    return when (this) {
        is ApiResult.Success -> ContentUiState.Success(data)
        is ApiResult.SuccessNoResponse -> ContentUiState.SuccessNoResponse
        is ApiResult.Error -> ContentUiState.Error(message)
    }
}