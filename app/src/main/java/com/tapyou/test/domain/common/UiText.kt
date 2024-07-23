package com.tapyou.test.domain.common

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed class UiText {
    data class DynamicString(val value: String) : UiText()

    data class StringResource(
        @StringRes val stringResource: Int,
        val formatArgs: List<Any> = emptyList()
    ) : UiText()

    @Composable
    fun getText(): String {
        return when (this) {
            is DynamicString -> value
            is StringResource -> stringResource(stringResource, *formatArgs.toTypedArray())
        }
    }

    fun getText(context: Context): String {
        return when (this) {
            is DynamicString -> value
            is StringResource -> {
                context.getString(stringResource, *formatArgs.toTypedArray())
            }
        }
    }
}
