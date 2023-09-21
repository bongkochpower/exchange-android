package com.powersoftlab.exchange_android.network

import com.powersoftlab.exchange_android.model.base.BaseResponseModel
import com.powersoftlab.exchange_android.model.body.LoginRequestModel
import com.powersoftlab.exchange_android.model.body.RegisterRequestModel
import com.powersoftlab.exchange_android.model.body.RequestNewCardRequestModel
import com.powersoftlab.exchange_android.model.response.AccessTokenModel
import com.powersoftlab.exchange_android.model.response.AddressAutoFillResponseModel
import com.powersoftlab.exchange_android.model.response.AuthSecretKeyModel
import com.powersoftlab.exchange_android.model.response.CardsResponseModel
import com.powersoftlab.exchange_android.model.response.ExchangeCalculateResponse
import com.powersoftlab.exchange_android.model.response.PinResponseModel
import com.powersoftlab.exchange_android.model.response.RegisterResponseModel
import com.powersoftlab.exchange_android.model.response.RequestNewCardResponseModel
import com.powersoftlab.exchange_android.model.response.TopUpResponse
import com.powersoftlab.exchange_android.model.response.TransactionsModel
import com.powersoftlab.exchange_android.model.response.UploadResponseModel
import com.powersoftlab.exchange_android.model.response.UserModel
import com.powersoftlab.exchange_android.model.response.WithdrawResponseModel
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface AppAPI {

    //region authorization
    @POST("api/v1/customer/login")
    suspend fun login(
        @Body loginRequest: LoginRequestModel
    ): Response<BaseResponseModel<AccessTokenModel>>

    @FormUrlEncoded
    @POST("api/v1/customer/login/social")
    suspend fun loginSocial(
        @Field("social") social: String,
        @Field("access_token") token: String
    ): Response<BaseResponseModel<AccessTokenModel>>

    @POST("api/v1/customer")
    suspend fun register(@Body request: RegisterRequestModel): Response<RegisterResponseModel>

    @PUT("api/v1/customer/{id}")
    suspend fun updateProfile(
        @Path("id") id : String,
        @Body request: RegisterRequestModel): Response<Any>


    @GET("api/v1/customer/me")
    suspend fun me(): Response<BaseResponseModel<UserModel>>

    @Multipart
    @POST("api/v1/upload")
    suspend fun uploadProfile(@Part image: MultipartBody.Part): Response<UploadResponseModel>

    @GET("api/v1/pin")
    suspend fun getAuthSecretKey(): Response<AuthSecretKeyModel>

    @FormUrlEncoded
    @POST("api/v1/pin")
    suspend fun createPin(
        @Field("pin") pin: String,
        @Field("secret") secretKey: String,
    ): Response<PinResponseModel>

    @FormUrlEncoded
    @POST("api/v1/pin/checkPin")
    suspend fun checkPin(
        @Field("pin") pin: String,
    ): Response<PinResponseModel>

    //endregion

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

    @FormUrlEncoded
    @POST("api/v1/transaction/topup")
    suspend fun topUp(
        @Field("currency_id") currencyId: Int = 1,
        @Field("value") value: Double
    ): Response<TopUpResponse>

    @FormUrlEncoded
    @POST("api/v1/exchange")
    suspend fun exchange(
        @Field("currency_main") currencyFromID: Int,
        @Field("currency_to") currencyToID: Int,
        @Field("amount") amount: Double
    ): Response<TopUpResponse>

    @FormUrlEncoded
    @POST("api/v1/exchange/cal")
    suspend fun exchangeCalculate(
        @Field("currency_main") currencyFromID: Int,
        @Field("currency_to") currencyToID: Int,
        @Field("amount") amount: Double
    ): Response<ExchangeCalculateResponse>

    @GET("api/v1/address/getSubDistrict")
    suspend fun subDistricts(): Response<BaseResponseModel<List<AddressAutoFillResponseModel.SubDistrictResponse>>>

    @GET("api/v1/address/getProvinceAndDistrict")
    suspend fun getAddressBySubDistrictId(
        @Query("subDistrictId") subDistrictId: Int,
    ): Response<BaseResponseModel<AddressAutoFillResponseModel>>

    @GET("api/v1/card")
    suspend fun getCards(): Response<BaseResponseModel<List<CardsResponseModel>>>

    @POST("api/v1/card/requestCard")
    suspend fun registerNewCard(
        @Body request : RequestNewCardRequestModel
    ): Response<RequestNewCardResponseModel>

    @GET("api/v1/country")
    suspend fun getCountry(
        @Query("page") page : Int,
        @Query("limit") limit : Int,
    ): Response<BaseResponseModel<List<UserModel.CountryModel>>>

    @GET("api/v1/shop")
    suspend fun getShopsByCountryId(
        @Query("country_id") countryId : Int,
        @Query("page") page : Int,
        @Query("limit") limit : Int
    ): Response<BaseResponseModel<List<UserModel.ShopModel>>>

    @FormUrlEncoded
    @POST("api/v1/transaction/cashout")
    suspend fun withdraw(
        @Field("currency_id") currencyId: Int,
        @Field("value") value: Double,
        @Field("shop_id") shopId: Int
    ): Response<WithdrawResponseModel>

    //endregion

}