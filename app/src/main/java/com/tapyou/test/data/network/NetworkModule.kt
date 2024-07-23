package com.tapyou.test.data.network

import android.util.Log
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.tapyou.test.data.network.adapters.ApiResultAdapterFactory
import com.tapyou.test.data.network.interceptors.TimeoutInterceptor
import kotlinx.serialization.ExperimentalSerializationApi
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import retrofit2.Retrofit
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

@OptIn(ExperimentalSerializationApi::class)
val networkModule =
    module {

        single {
            val httpBuilder = OkHttpClient.Builder()

            httpBuilder
                .addInterceptor(get<HttpLoggingInterceptor>())
                .hostnameVerifier { _, _ -> true }
                .addInterceptor(get<TimeoutInterceptor>())
                .build()
        }

        single {
            val contentType = "application/json".toMediaType()
            val json =
                kotlinx.serialization.json.Json {
                    explicitNulls = false
                    ignoreUnknownKeys = true
                    isLenient = true
                }

            Retrofit.Builder()
                .baseUrl("https://hr-challenge.dev.tapyou.com/")
                .client(get())
                .addConverterFactory(json.asConverterFactory(contentType))
                .addCallAdapterFactory(ApiResultAdapterFactory())
                .build()
        }


        singleOf(::TimeoutInterceptor)
        single {
            HttpLoggingInterceptor { message ->
                Log.d("Http", message)
            }.apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        }
    }
