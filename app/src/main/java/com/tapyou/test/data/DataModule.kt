package com.tapyou.test.data

import com.tapyou.test.data.api.PointsApi
import com.tapyou.test.data.repository.PointsRepositoryImpl
import com.tapyou.test.domain.repository.PointsRepository
import org.koin.dsl.module
import retrofit2.Retrofit

internal val dataModule = module {
    single<PointsRepository> { PointsRepositoryImpl(get()) }
    single { get<Retrofit>().create(PointsApi::class.java) }
}