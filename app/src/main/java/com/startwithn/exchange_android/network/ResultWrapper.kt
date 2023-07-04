package com.startwithn.exchange_android.network

import com.startwithn.exchange_android.common.enum.AppEventEnum

sealed class ResultWrapper<out T: Any> {
    data class Success<T : Any>(val response: T): ResultWrapper<T>()
    data class GenericError(val code : Int?, val message: String?) : ResultWrapper<Nothing>()
    data class AppEvent(val appEvent: AppEventEnum, val message: String?) : ResultWrapper<Nothing>()
    data class NetworkError(val title: String?, val message: String?): ResultWrapper<Nothing>()
    object Loading: ResultWrapper<Nothing>()
    object Empty: ResultWrapper<Nothing>()
}