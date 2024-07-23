package com.tapyou.test.domain

import com.tapyou.test.domain.usecase.FetchPointsUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val domainModule = module {
    singleOf(::FetchPointsUseCase)
}