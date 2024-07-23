package com.tapyou.test.domain.common

import androidx.compose.runtime.Immutable

@Immutable
sealed interface ResourceUiState<out T> {
    data class Success<T>(val data: T) : ResourceUiState<T>

    data object SuccessNoResponse : ResourceUiState<Nothing>

    data class Error(val error: UiText) : ResourceUiState<Nothing>
    data object Loading : ResourceUiState<Nothing>
    data object Empty : ResourceUiState<Nothing>
    data object Idle : ResourceUiState<Nothing>
}
