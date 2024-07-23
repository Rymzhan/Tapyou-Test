package com.tapyou.test.presentation.ui.details

import androidx.lifecycle.viewModelScope
import com.tapyou.test.domain.common.ApiResult.Companion.onError
import com.tapyou.test.domain.common.ApiResult.Companion.onSuccess
import com.tapyou.test.domain.common.ContentUiState
import com.tapyou.test.domain.common.UiText
import com.tapyou.test.domain.model.Points
import com.tapyou.test.domain.usecase.FetchPointsUseCase
import com.tapyou.test.presentation.common.BaseAction
import com.tapyou.test.presentation.common.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PointsViewModel(
    private val pointsUseCase: FetchPointsUseCase
) : BaseViewModel<PointsContract.State, PointsContract.Effect>() {

    override fun createInitialState(): PointsContract.State = PointsContract.State()

    override fun processAction(action: BaseAction) {
        when (action) {
            is PointsContract.Action.OnFetchPointsData -> fetchPointsData(action.count)
        }
    }

    private fun fetchPointsData(count: Int) {
        updateState(ContentUiState.Loading())
        viewModelScope.launch {
            delay(1000)
            pointsUseCase.invoke(count)
                .onSuccess(::handleSuccess)
                .onError(::handleError)
        }
    }

    private fun updateState(state: ContentUiState<List<Points>>) {
        setState {
            copy(pointsContent = state)
        }
    }

    private fun handleSuccess(points: List<Points>) {
        updateState(ContentUiState.Success(points))
    }

    private fun handleError(message: UiText, code: Int?, messageBody: String?) {
        updateState(ContentUiState.Error(message))
    }
}