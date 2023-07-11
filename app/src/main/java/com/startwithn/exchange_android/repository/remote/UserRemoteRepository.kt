package com.startwithn.exchange_android.repository.remote

import android.content.Context
import com.startwithn.exchange_android.common.manager.AppManager
import com.startwithn.exchange_android.ext.toSHA256
import com.startwithn.exchange_android.model.base.BaseResponseModel
import com.startwithn.exchange_android.model.body.LoginRequestModel
import com.startwithn.exchange_android.model.body.RegisterRequestModel
import com.startwithn.exchange_android.model.response.AccessTokenModel
import com.startwithn.exchange_android.model.response.MessageModel
import com.startwithn.exchange_android.model.response.RegisterResponseModel
import com.startwithn.exchange_android.model.response.UploadResponseModel
import com.startwithn.exchange_android.model.response.UserModel
import com.startwithn.exchange_android.network.AppAPI
import com.startwithn.exchange_android.network.ResultWrapper
import com.startwithn.exchange_android.repository.remote.base.BaseRemoteRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.MultipartBody

class UserRemoteRepository(
    private val context: Context,
    private val api: AppAPI,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseRemoteRepository(context) {
    suspend fun login(
        request: LoginRequestModel
    ): ResultWrapper<BaseResponseModel<AccessTokenModel>> {
        return safeApiCall(
            dispatcher,
            call = {
                api.login(request)
            })
    }

    suspend fun register(
        request: RegisterRequestModel
    ): ResultWrapper<RegisterResponseModel>{
        return safeApiCall(
            dispatcher,
            call = {
                api.register(request)
            })
    }

    suspend fun uploadAvatar(avatar: MultipartBody.Part): ResultWrapper<UploadResponseModel> {
        return safeApiCall(dispatcher, call = { api.uploadProfile(avatar) })
    }

}