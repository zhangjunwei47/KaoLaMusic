package com.kaola.lib_network

import com.kaola.lib_network.util.NetworkConstant
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitFactory private constructor() {

    companion object {
        val instance: RetrofitFactory by lazy { RetrofitFactory() }
    }

    private val mRetrofit: Retrofit
    private val mInterceptor: Interceptor

    init {
        mInterceptor = Interceptor { chain ->
            val request = chain.request()
                .newBuilder()
                .header("Content-Type", "application/json")
                .addHeader("charset", "utf-8")
                .build()
            chain.proceed(request)
        }
        mRetrofit = Retrofit.Builder()
            .baseUrl(NetworkConstant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .client(initClient())
            .build()
    }

    /**
     * 初始化okhttpclient
     */
    private fun initClient(): OkHttpClient? {
        return OkHttpClient.Builder()
            .addInterceptor(mInterceptor)
            .addInterceptor(initLogInterceptor())
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .build()
    }

    /**
     * 日志拦截器
     */
    private fun initLogInterceptor(): Interceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    fun <T> create(service: Class<T>): T {
        return mRetrofit.create(service)
    }
}