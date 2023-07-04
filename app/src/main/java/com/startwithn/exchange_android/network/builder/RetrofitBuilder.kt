package com.startwithn.exchange_android.network.builder

import android.content.Context
import com.startwithn.exchange_android.network.AppAPI
import com.startwithn.exchange_android.network.interceptor.AuthorizationInterceptor
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitBuilder(
    private val context: Context,
    private val baseUrl: String,
    private val appId: String,
    private val appVersion: String
) {

    companion object {
        private const val TIME_OUT: Long = 60L
    }

    private fun getDefaultOkHttpClient(interceptor: Interceptor): OkHttpClient =
        OkHttpClient.Builder().apply {
            connectTimeout(TIME_OUT, TimeUnit.SECONDS)
            readTimeout(TIME_OUT, TimeUnit.SECONDS)
            addInterceptor(interceptor)
        }.build()

    private fun getDefaultRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(getDefaultOkHttpClient(AuthorizationInterceptor(context, appId, appVersion)))
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()


    fun getAppAPI(): AppAPI = getDefaultRetrofit().create(AppAPI::class.java)

}