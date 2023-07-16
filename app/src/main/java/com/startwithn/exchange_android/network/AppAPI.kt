package com.startwithn.exchange_android.network

import com.startwithn.exchange_android.common.constant.AppConstant
import com.startwithn.exchange_android.model.base.BaseResponseModel
import com.startwithn.exchange_android.model.body.LoginRequestModel
import com.startwithn.exchange_android.model.body.RegisterRequestModel
import com.startwithn.exchange_android.model.response.AccessTokenModel
import com.startwithn.exchange_android.model.response.RegisterResponseModel
import com.startwithn.exchange_android.model.response.TransactionsModel
import com.startwithn.exchange_android.model.response.UploadResponseModel
import com.startwithn.exchange_android.model.response.UserModel
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface AppAPI {

    //region authorization
    @POST("api/v1/customer/login")
    suspend fun login(
        @Body loginRequest : LoginRequestModel
    ): Response<BaseResponseModel<AccessTokenModel>>

    @POST("api/v1/customer")
    suspend fun register(@Body request: RegisterRequestModel): Response<RegisterResponseModel>

    @GET("api/v1/customer/me")
    suspend fun me(): Response<BaseResponseModel<UserModel>>
    //endregion

    @Multipart
    @POST("api/v1/upload")
    suspend fun uploadProfile(@Part image: MultipartBody.Part): Response<UploadResponseModel>

    //region app func

    @GET("api/v1/transaction/customer")
    suspend fun lastTransactions(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
    ): Response<BaseResponseModel<List<TransactionsModel>>>

    @GET("api/v1/transaction/customer")
    suspend fun historyTransactions(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("dateFrom") dateFrom: String,
        @Query("dateTo") dateTo: String,
    ): Response<BaseResponseModel<List<TransactionsModel>>>


    //endregion

}