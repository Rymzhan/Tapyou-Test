package com.tapyou.test.data.repository

import com.tapyou.test.data.api.PointsApi
import com.tapyou.test.data.model.PointsMapper
import com.tapyou.test.domain.common.ApiResult
import com.tapyou.test.domain.model.Points
import com.tapyou.test.domain.repository.PointsRepository

class PointsRepositoryImpl(
    private val api: PointsApi
) : PointsRepository {
    override suspend fun fetchPointsData(count: Int): ApiResult<List<Points>> {
        return PointsMapper.map(api.getPaymentCategories(count))
    }
}