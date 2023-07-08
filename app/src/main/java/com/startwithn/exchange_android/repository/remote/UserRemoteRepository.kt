package com.startwithn.exchange_android.repository.remote

import android.content.Context
import com.startwithn.exchange_android.common.manager.AppManager
import com.startwithn.exchange_android.ext.toSHA256
import com.startwithn.exchange_android.model.base.BaseResponseModel
import com.startwithn.exchange_android.model.body.LoginRequestModel
import com.startwithn.exchange_android.model.response.MessageModel
import com.startwithn.exchange_android.model.response.UserModel
import com.startwithn.exchange_android.network.AppAPI
import com.startwithn.exchange_android.network.ResultWrapper
import com.startwithn.exchange_android.repository.remote.base.BaseRemoteRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class UserRemoteRepository(
    private val context: Context,
    private val api: AppAPI,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseRemoteRepository(context) {

    /*suspend fun sendOtp(phoneNumber: String?): ResultWrapper<DataModel<OtpModel>> {
        return safeApiCall(dispatcher, call = { api.sendOtp(phoneNumber) })
    }


    suspend fun verifies(
        phoneNumber: String?,
        code: String?,
        reference: String?
    ): ResultWrapper<DataModel<MessageModel>> {
        return safeApiCall(dispatcher, call = { api.verifies(phoneNumber, code, reference) })
    }

    suspend fun verifiesEmail(
        email: String?,
        code: String?,
        reference: String?
    ): ResultWrapper<DataModel<MessageModel>> {
        return safeApiCall(dispatcher, call = { api.verifiesEmail(email, code, reference) })
    }

    suspend fun passwords(
        phoneNumber: String?,
        password: String?,
        passwordConfirmation: String?,
        isConsent: Boolean?,
        email: String?,
    ): ResultWrapper<DataTokenModel<UserModel>> {
        return safeApiCall(
            dispatcher,
            call = {
                api.passwords(
                    phoneNumber = phoneNumber,
                    password = password?.toSHA256(),
                    passwordConfirmation = passwordConfirmation?.toSHA256(),
                    fcmToken = AppManager(context).getFcmToken(),
                    isConsent = if(isConsent==true){1}else{0},
                    email = email
                )
            })
    }
*/
    suspend fun login(
        request: LoginRequestModel
    ): ResultWrapper<BaseResponseModel<UserModel>> {
        return safeApiCall(
            dispatcher,
            call = {
                api.login(
                    phoneNumber = request.email,
                    password = request.password.toSHA256()
                )
            })
    }
}