package com.tapyou.test.presentation.common

interface BaseAction {
    data object OnRetry : BaseAction
    data object OnPullToRefresh : BaseAction
}
