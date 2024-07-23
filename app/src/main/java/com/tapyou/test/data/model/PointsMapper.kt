package com.tapyou.test.data.model

import com.tapyou.test.domain.common.BaseMapper
import com.tapyou.test.domain.model.Points

object PointsMapper : BaseMapper<PointsResponse, List<Points>>() {
    override fun mapData(from: PointsResponse): List<Points> {
        val pointsList = from.points.map { pointsData ->
            Points(pointsData.x, pointsData.y)
        }

        return pointsList
    }
}