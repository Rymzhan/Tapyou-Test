package com.tapyou.test.data.network.adapters

import android.accounts.NetworkErrorException
import com.tapyou.test.R
import com.tapyou.test.domain.common.ApiResult
import com.tapyou.test.domain.common.ErrorResponse
import com.tapyou.test.domain.common.UiText
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException

internal class ApiResultCall<T>(
    private val callDelegate: Call<T>,
) : Call<ApiResult<T>> {

    override fun enqueue(callback: Callback<ApiResult<T>>) = callDelegate.enqueue(
        object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                when (response.code()) {
                    in 200..208 -> {
                        response.body()?.let {
                            callback.onResponse(
                                this@ApiResultCall, Response.success(
                                    ApiResult.Success(
                                        it
                                    )
                                )
                            )
                        } ?: callback.onResponse(
                            this@ApiResultCall, Response.success(
                                ApiResult.SuccessNoResponse()
                            )
                        )
                    }

                    in 400..409 -> {
                        callback.onResponse(
                            this@ApiResultCall,
                            Response.success(
                                response.convertToApiResult()
                            )
                        )
                    }

                    else -> {
                        callback.onResponse(
                            this@ApiResultCall,
                            Response.success(
                                ApiResult.Error(
                                    UiText.StringResource(R.string.something_went_wrong),
                                    response.code(),
                                    response.errorBody()?.string()
                                )
                            )
                        )
                    }
                }
            }

            override fun onFailure(call: Call<T>, throwable: Throwable) {
                callback.onResponse(
                    this@ApiResultCall, Response.success(
                        throwable.catchError()
                    )
                )
                call.cancel()
            }
        },
    )

    override fun clone(): Call<ApiResult<T>> = ApiResultCall(callDelegate.clone())

    override fun execute(): Response<ApiResult<T>> =
        throw UnsupportedOperationException("ResponseCall does not support execute.")

    override fun isExecuted(): Boolean = callDelegate.isExecuted

    override fun cancel() = callDelegate.cancel()

    override fun isCanceled(): Boolean = callDelegate.isCanceled

    override fun request(): Request = callDelegate.request()

    override fun timeout(): Timeout = callDelegate.timeout()
}

@OptIn(ExperimentalSerializationApi::class)
private fun <T, T1> Response<T>?.convertToApiResult(): ApiResult<T1> {
    return try {
        val errorBodyString = this?.errorBody()?.string()
        val json = Json {
            ignoreUnknownKeys = true
            explicitNulls = false
        }
        val errorParsed = json.decodeFromString<ErrorResponse>(errorBodyString.orEmpty())
        ApiResult.Error(
            UiText.DynamicString(
                errorParsed.errorMessage
                    ?: errorParsed.errorDescription.orEmpty()
            ),
            errorParsed.errorCode,
            errorBodyString
        )
    } catch (e: Exception) {
        e.printStackTrace()
        ApiResult.Error(
            UiText.StringResource(R.string.something_went_wrong),
            this?.code(),
            this?.errorBody()?.string()
        )
    }
}

private fun <T> Throwable.catchError(): ApiResult<T> {
    this.printStackTrace()
    return when (this) {
        is HttpException -> {
            this.response().convertToApiResult()
        }

        is UnknownHostException, is NetworkErrorException, is SocketTimeoutException -> {
            ApiResult.Error(UiText.StringResource(R.string.no_internet_connection))
        }

        else -> {
            ApiResult.Error(UiText.StringResource(R.string.something_went_wrong))
        }
    }
}