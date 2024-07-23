package com.tapyou.test.data.api

import com.tapyou.test.data.model.PointsResponse
import com.tapyou.test.domain.common.ApiResult
import retrofit2.http.GET
import retrofit2.http.Query

interface PointsApi {

    @GET("api/test/points")
    suspend fun getPaymentCategories(
        @Query("count") count: Int
    ): ApiResult<PointsResponse>
}