package com.powersoftlab.exchange_android.repository.remote

import android.content.Context
import com.powersoftlab.exchange_android.model.base.BaseResponseModel
import com.powersoftlab.exchange_android.model.body.LoginRequestModel
import com.powersoftlab.exchange_android.model.body.LoginSocialRequestModel
import com.powersoftlab.exchange_android.model.body.RegisterRequestModel
import com.powersoftlab.exchange_android.model.response.AccessTokenModel
import com.powersoftlab.exchange_android.model.response.AuthSecretKeyModel
import com.powersoftlab.exchange_android.model.response.PinResponseModel
import com.powersoftlab.exchange_android.model.response.RegisterResponseModel
import com.powersoftlab.exchange_android.model.response.UploadResponseModel
import com.powersoftlab.exchange_android.model.response.UserModel
import com.powersoftlab.exchange_android.network.AppAPI
import com.powersoftlab.exchange_android.network.ResultWrapper
import com.powersoftlab.exchange_android.repository.remote.base.BaseRemoteRepository
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

    suspend fun loginSocial(
        request: LoginSocialRequestModel
    ): ResultWrapper<BaseResponseModel<AccessTokenModel>> {
        return safeApiCall(
            dispatcher,
            call = {
                api.loginSocial(
                    social = request.social,
                    token = request.accessToken
                )
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

    suspend fun updateProfile(
        userId : String,
        request: RegisterRequestModel
    ): ResultWrapper<Any>{
        return safeApiCall(
            dispatcher,
            call = {
                api.updateProfile(userId,request)
            })
    }

    suspend fun uploadAvatar(avatar: MultipartBody.Part): ResultWrapper<UploadResponseModel> {
        return safeApiCall(dispatcher, call = { api.uploadProfile(avatar) })
    }

    suspend fun me() : ResultWrapper<BaseResponseModel<UserModel>>{
        return safeApiCall(dispatcher, call = { api.me() })
    }

    suspend fun checkPin(pin : String) : ResultWrapper<PinResponseModel>{
        return safeApiCall(dispatcher, call = { api.checkPin(pin) })
    }

    suspend fun createPin(pin : String,secretKey : String) : ResultWrapper<PinResponseModel>{
        return safeApiCall(dispatcher, call = { api.createPin(pin, secretKey) })
    }

    suspend fun getSecretKey() : ResultWrapper<AuthSecretKeyModel>{
        return safeApiCall(dispatcher, call = { api.getAuthSecretKey() })
    }

}