package com.powersoftlab.exchange_android.network.interceptor

import android.content.Context
import com.powersoftlab.exchange_android.common.constant.AppConstant
import com.powersoftlab.exchange_android.common.manager.AppManager
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AuthorizationInterceptor(private val context: Context, private val appId: String, private val appVersion: String) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token: String? = AppManager(context).getAuthToken()

//        val location: Location? = null
//        val lat: Double = location?.latitude ?: 0.0
//        val lng: Double = location?.longitude ?: 0.0

        val deviceType = AppConstant.DEVICE_TYPE

        val language = "th"

        val original: Request = chain.request()
        val request: Request = original.newBuilder()
            .addHeader("Content-Type", "application/json")
            .addHeader("User-Agent", "$appId $deviceType")
            .addHeader("Accept", "application/json")
            .addHeader("Authorization", "Bearer $token")
            .addHeader("Accept-Language", language)
            //.addHeader("FV-App-Version", appVersion)
            //.addHeader("FV-Client", deviceType)
            //.addHeader("FV-Data", "$lat|$lng")
            .addHeader("app_name", "exchange")
            .method(original.method, original.body)
            .build()
        return chain.proceed(request)
    }
}