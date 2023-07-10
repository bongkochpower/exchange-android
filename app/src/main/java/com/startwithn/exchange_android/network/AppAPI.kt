package com.startwithn.exchange_android.network

import com.startwithn.exchange_android.common.constant.AppConstant
import com.startwithn.exchange_android.model.base.BaseResponseModel
import com.startwithn.exchange_android.model.body.RegisterRequestModel
import com.startwithn.exchange_android.model.response.AccessTokenModel
import com.startwithn.exchange_android.model.response.RegisterResponseModel
import com.startwithn.exchange_android.model.response.UploadResponseModel
import com.startwithn.exchange_android.model.response.UserModel
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface AppAPI {

    //region authorization
    @FormUrlEncoded
    @POST("api/v1/customer/login")
    suspend fun login(
        @Field("mobile") phoneNumber: String?,
        @Field("password") password: String?,
    ): Response<BaseResponseModel<AccessTokenModel>>

    @POST("api/v1/customer")
    suspend fun register(@Body request: RegisterRequestModel): Response<RegisterResponseModel>


    @Multipart
    @POST("api/v1/upload")
    suspend fun uploadProfile(@Part image: MultipartBody.Part): Response<UploadResponseModel>
    //endregion
}