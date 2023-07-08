package com.startwithn.exchange_android.repository.remote.base

import android.content.Context
import com.startwithn.exchange_android.network.ResultWrapper
import com.google.gson.Gson

import com.startwithn.exchange_android.common.constant.ResponseCodeConstant
import com.startwithn.exchange_android.common.enum.AppEventEnum
import com.startwithn.exchange_android.common.rx.RxBus
import com.startwithn.exchange_android.common.rx.RxEvent
import com.startwithn.exchange_android.R
import com.startwithn.exchange_android.model.response.MessageModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import timber.log.Timber
import java.io.IOException

open class BaseRemoteRepository(private val context: Context) {
    suspend fun <T : Any> safeApiCall(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        call: suspend () -> Response<T>
    ): ResultWrapper<T> {
        return withContext(dispatcher) {
            try {
                val response = call.invoke()
                Timber.d("call api -> $response")
                handleEventApp(response)
            } catch (throwable: Throwable) {
                when (throwable) {
                    is IOException -> ResultWrapper.NetworkError(
                        context.getString(R.string.message_no_internet),
                        context.getString(R.string.message_please_check_no_internet)
                    )
                    else -> ResultWrapper.GenericError(0, context.getString(R.string.message_error))
                }
            }
        }
    }

    private fun <T : Any> handleEventApp(response: Response<T>): ResultWrapper<T> {
        //error response
        val code = response.code()
        val messageModel: MessageModel = getApiError(response)
        val errorResponse = messageModel.message
        //header
        val headers = response.headers()
        // version|event
        val versionOrEventList = headers["FV-App-Version"]?.split("|").orEmpty()
        val version = versionOrEventList.firstOrNull().toString()
        val eventApp = if (versionOrEventList.size >= 2) {
            AppEventEnum.fromCode(versionOrEventList[1])
        } else {
            AppEventEnum.NONE
        }
        return when {
            eventApp == AppEventEnum.NONE && response.isSuccessful -> {
                response.body()?.let {
                    ResultWrapper.Success(it)
                } ?: run {
                    ResultWrapper.GenericError(0, context.getString(R.string.message_error))
                }
            }
            eventApp == AppEventEnum.MAINTENANCE -> {
                RxBus.publish(RxEvent.AppEvent(eventApp, errorResponse))
                ResultWrapper.AppEvent(eventApp, errorResponse)
            }
            eventApp == AppEventEnum.SOFT_UPDATE -> {
                RxBus.publish(RxEvent.AppEvent(eventApp, version))
                ResultWrapper.AppEvent(eventApp, errorResponse)
            }
            eventApp == AppEventEnum.FORCE_UPDATE -> {
                RxBus.publish(RxEvent.AppEvent(eventApp))
                ResultWrapper.AppEvent(eventApp, errorResponse)
            }
            code == ResponseCodeConstant.UNAUTHORIZED -> {
                RxBus.publish(RxEvent.AppEvent(AppEventEnum.UNAUTHORIZED, errorResponse))
                ResultWrapper.AppEvent(eventApp, errorResponse)
            }
            else -> {
                ResultWrapper.GenericError(code, errorResponse)
            }
        }
    }

    private fun getApiError(response: Response<*>?): MessageModel {
        var message: String? = null
        try {
            response?.errorBody()?.let {
                message = it.string()
                return Gson().fromJson(message, MessageModel::class.java)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return MessageModel(message)
    }

}