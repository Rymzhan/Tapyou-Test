package com.tapyou.test.domain.common

abstract class BaseMapper<FROM, TO> {

    abstract fun mapData(from: FROM): TO

    fun map(from: ApiResult<FROM>): ApiResult<TO> = mapApiResult(from)

    private fun mapApiResult(from: ApiResult<FROM>): ApiResult<TO> = when (from) {
        is ApiResult.Error -> ApiResult.Error(from.message, from.code, from.messageBody)
        is ApiResult.Success -> ApiResult.Success(mapData(from.data))
        is ApiResult.SuccessNoResponse -> ApiResult.SuccessNoResponse()
    }
}