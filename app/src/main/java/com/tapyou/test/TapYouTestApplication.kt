package com.tapyou.test

import android.app.Application
import com.tapyou.test.data.dataModule
import com.tapyou.test.data.network.networkModule
import com.tapyou.test.domain.domainModule
import com.tapyou.test.presentation.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext

class TapYouTestApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        initKoin()
    }

    private fun initKoin() {
        GlobalContext.startKoin {
            androidLogger()
            androidContext(this@TapYouTestApplication)

            modules(networkModule)
            modules(dataModule)
            modules(domainModule)
            modules(presentationModule)
        }
    }
}