package com.tapyou.test.data.network.interceptors

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.util.concurrent.TimeUnit

class TimeoutInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val r: Request = chain.request()

        var connectTimeout = chain.connectTimeoutMillis()
        var readTimeout = chain.readTimeoutMillis()
        var writeTimeout = chain.writeTimeoutMillis()

        val connectNew: String? = r.header(CONNECT_TIMEOUT)
        val readNew: String? = r.header(READ_TIMEOUT)
        val writeNew: String? = r.header(WRITE_TIMEOUT)

        if (!connectNew.isNullOrEmpty()) {
            connectTimeout = Integer.valueOf(connectNew)
        }
        if (!readNew.isNullOrEmpty()) {
            readTimeout = Integer.valueOf(readNew)
        }
        if (!writeNew.isNullOrEmpty()) {
            writeTimeout = Integer.valueOf(writeNew)
        }
        return chain
            .withConnectTimeout(connectTimeout, TimeUnit.SECONDS)
            .withReadTimeout(readTimeout, TimeUnit.SECONDS)
            .withWriteTimeout(writeTimeout, TimeUnit.SECONDS)
            .proceed(r)
    }

    companion object {
        const val CONNECT_TIMEOUT = "CONNECT_TIMEOUT"
        const val READ_TIMEOUT = "READ_TIMEOUT"
        const val WRITE_TIMEOUT = "WRITE_TIMEOUT"
    }
}