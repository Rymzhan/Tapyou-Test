package com.tapyou.test.domain.usecase

import com.tapyou.test.domain.repository.PointsRepository

class FetchPointsUseCase(
    private val pointsRepository: PointsRepository
) {
    suspend operator fun invoke(count: Int) = pointsRepository.fetchPointsData(count)
}