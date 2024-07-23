package com.tapyou.test.domain.common

sealed interface ContentUiState<out T> {
    data class Success<T>(val data: T) : ContentUiState<T>
    data object SuccessNoResponse : ContentUiState<Nothing>
    data class Error(val error: UiText) : ContentUiState<Nothing>
    data class Loading<T>(val data: T? = null) : ContentUiState<T>
}
