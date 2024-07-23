package com.tapyou.test.presentation.ui.details

import com.tapyou.test.domain.common.ContentUiState
import com.tapyou.test.domain.model.Points
import com.tapyou.test.presentation.common.BaseAction
import com.tapyou.test.presentation.common.BaseEffect
import com.tapyou.test.presentation.common.BaseState

interface PointsContract {

    sealed interface Action : BaseAction {
        data class OnFetchPointsData(
            val count: Int
        ) : Action
    }

    data class State(
        val pointsContent: ContentUiState<List<Points>> = ContentUiState.Loading(),
    ) : BaseState

    sealed interface Effect : BaseEffect

}