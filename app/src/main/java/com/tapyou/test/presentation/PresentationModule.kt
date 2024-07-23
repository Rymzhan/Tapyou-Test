package com.tapyou.test.presentation

import com.tapyou.test.presentation.ui.details.PointsViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val presentationModule = module {
    viewModelOf(::PointsViewModel)
}