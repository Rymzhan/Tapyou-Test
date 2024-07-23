package com.tapyou.test.domain.repository

import com.tapyou.test.domain.common.ApiResult
import com.tapyou.test.domain.model.Points

interface PointsRepository {

    suspend fun fetchPointsData(count: Int): ApiResult<List<Points>>

}