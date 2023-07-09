package com.startwithn.exchange_android.network.builder

import android.content.Context
import com.startwithn.exchange_android.network.AppAPI
import com.startwithn.exchange_android.network.interceptor.AuthorizationInterceptor
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.startwithn.exchange_android.BuildConfig
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitBuilder(
    private val context: Context
) {

    companion object {
        private const val TIME_OUT: Long = 60L
        val MEDIA_TYPE_FILE = "multipart/form-result".toMediaTypeOrNull()
    }

    fun api(): AppAPI {
        val okHttpClient = OkHttpClient.Builder()
        okHttpClient.connectTimeout(TIME_OUT, TimeUnit.SECONDS)
        okHttpClient.readTimeout(TIME_OUT, TimeUnit.SECONDS)
        okHttpClient.addInterceptor(AuthorizationInterceptor(context,BuildConfig.APPLICATION_ID,BuildConfig.VERSION_NAME))

        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient.build())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build().create(AppAPI::class.java)
    }

}