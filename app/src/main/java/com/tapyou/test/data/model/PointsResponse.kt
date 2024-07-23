package com.tapyou.test.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PointsResponse(
    @SerialName("points")
    val points: List<PointsData>
)

@Serializable
data class PointsData(
    @SerialName("x")
    val x: Double?,
    @SerialName("y")
    val y: Double?
)