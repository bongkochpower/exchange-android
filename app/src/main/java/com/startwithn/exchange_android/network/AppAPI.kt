package com.startwithn.exchange_android.network

import com.startwithn.exchange_android.common.constant.AppConstant
import com.startwithn.exchange_android.model.MessageModel
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface AppAPI {

    //region authorization
    /*@FormUrlEncoded
    @POST("api/v1/send-otp")
    suspend fun sendOtp(
        @Field("phone_number") phoneNumber: String?,
    ): Response<DataModel<OtpModel>>

    @FormUrlEncoded
    @POST("api/v1/send-otp/email")
    suspend fun sendOtpEmail(
        @Field("email") email: String?,
    ): Response<DataModel<OtpModel>>

    @FormUrlEncoded
    @POST("api/v1/verifies")
    suspend fun verifies(
        @Field("phone_number") phoneNumber: String?,
        @Field("code") code: String?,
        @Field("reference") reference: String?
    ): Response<DataModel<MessageModel>>

    @FormUrlEncoded
    @POST("api/v1/verify-email")
    suspend fun verifiesEmail(
        @Field("email") email: String?,
        @Field("code") code: String?,
        @Field("reference") reference: String?
    ): Response<DataModel<MessageModel>>

    @FormUrlEncoded
    @POST("api/v1/passwords")
    suspend fun passwords(
        @Field("phone_number") phoneNumber: String?,
        @Field("password") password: String?,
        @Field("password_confirmation") passwordConfirmation: String?,
        @Field("fcm_token") fcmToken: String?,
        @Field("is_consent") isConsent: Int?,
        @Field("email") email: String?,
        @Header("client-secret") clientSecret: String = AppConstant.CLIENT_SECRET,
        @Header("client-id") clientId: String = AppConstant.CLIENT_ID
    ): Response<DataTokenModel<UserModel>>

    @FormUrlEncoded
    @POST("api/v1/login")
    suspend fun login(
        @Field("phone_number") phoneNumber: String?,
        @Field("password") password: String?,
        @Field("fcm_token") fcmToken: String?,
        @Field("email") email: String?,
        @Header("client-secret") clientSecret: String = AppConstant.CLIENT_SECRET,
        @Header("client-id") clientId: String = AppConstant.CLIENT_ID
    ): Response<DataTokenModel<UserModel>>
    //endregion

    //region product
    @GET("api/v1/banners")
    suspend fun banners(): Response<DataModel<MutableList<BannerModel>>>

    @GET("api/v1/categories")
    suspend fun categories(): Response<DataModel<MutableList<CategoryModel>>>

    @GET("api/v1/products")
    suspend fun products(
        @Query("category") categoryId: Int? = null,
        @Query("q") query: String? = null,
        @Query("page") page: Int? = null
    ): Response<DataPaginationModel<MutableList<ProductModel>>>

    @GET("api/v1/products/{id}")
    suspend fun products(
        @Path("id") productId: Int? = null,
        @Query("youtube_id") youtubeId: String? = null
    ): Response<DataModel<ProductModel>>

    @POST("api/v1/favorites/{id}/toggles")
    suspend fun toggles(
        @Path("id") productId: Int? = null
    ): Response<DataModel<MessageModel>>

    @GET("api/v1/favorites")
    suspend fun favorites(): Response<DataModel<MutableList<ProductModel>>>

    @GET("api/v1/relates")
    suspend fun relates(
        @Query("relate_id") relateId: Int? = null,
        @Query("product_id") productId: Int? = null
    ): Response<DataModel<MutableList<ProductModel>>>

    @GET("api/v1/flash-sales")
    suspend fun flashSales(): Response<DataModel<ProductModel>>
    //endregion

    //region order
    @POST("api/v1/orders")
    suspend fun orders(@Body createOrderModel: CreateOrderModel?): Response<DataModel<OrderModel>>

    @GET("api/v1/orders")
    suspend fun orders(
        @Query("status") status: Int?,
        @Query("page") page: Int? = null
    ): Response<DataPaginationModel<MutableList<OrderModel>>>

    @PATCH("api/v1/orders/{id}/cancel")
    suspend fun cancelOrders(
        @Path("id") orderId: Int?,
    ): Response<DataModel<MessageModel>>
    //endregion

    //region profile
    @GET("api/v1/me")
    suspend fun me(): Response<DataModel<UserModel>>

    @FormUrlEncoded
    @PATCH("api/v1/me")
    suspend fun me(
        @Field("first_name") firstName: String?,
        @Field("last_name") lastName: String?,
        @Field("birth_date") birthDate: String?,
        @Field("email") email: String?,
        @Field("phone_number") phoneNumber: String?
    ): Response<DataModel<UserModel>>

    @POST("api/v1/me")
    @Multipart
    suspend fun me(@Part avatar: MultipartBody.Part): Response<DataModel<UserModel>>

    @PATCH("api/v1/toggle-notification")
    suspend fun toggleNotification(): Response<DataModel<MessageModel>>

    @FormUrlEncoded
    @POST("api/v1/check-exists")
    suspend fun checkExists(
        @Field("phone_number") phoneNumber: String?,
        @Field("email") email: String?
    ): Response<DataModel<MessageModel>>

    @GET
    suspend fun findByZipCode(@Url url: String): Response<DataModel<AddressGroupModel>>

    @GET("api/v1/countries")
    suspend fun countries(): Response<DataModel<MutableList<CountryModel>>>

    @POST("api/v1/users/{id}/addresses")
    suspend fun address(
        @Path("id") userId: Int?,
        @Body addressModel: AddressModel?
    ): Response<DataModel<UserModel>>

    @PATCH("api/v1/users/{id}/addresses/{addressId}")
    suspend fun address(
        @Path("id") userId: Int?,
        @Path("addressId") addressId: Int?,
        @Body addressModel: AddressModel?
    ): Response<DataModel<UserModel>>

    @POST("api/v1/users/{id}/foreign")
    suspend fun foreign(
        @Path("id") userId: Int?,
        @Body addressModel: AddressModel?
    ): Response<DataModel<UserModel>>

    @PATCH("api/v1/users/{id}/foreign/{addressId}")
    suspend fun foreign(
        @Path("id") userId: Int?,
        @Path("addressId") addressId: Int?,
        @Body addressModel: AddressModel?
    ): Response<DataModel<UserModel>>

    @DELETE("api/v1/users/{id}/addresses/{addressId}")
    suspend fun address(
        @Path("id") userId: Int?,
        @Path("addressId") addressId: Int?
    ): Response<DataModel<MessageModel>>

    @FormUrlEncoded
    @PATCH("api/v1/users/{id}/passwords")
    suspend fun passwords(
        @Path("id") userId: Int?,
        @Field("old_password") oldPassword: String?,
        @Field("password") password: String?,
        @Field("password_confirmation") passwordConfirmation: String?
    ): Response<DataModel<UserModel>>
    //endregion

    //region payment
    @POST("api/v1/payments")
    suspend fun createCreditCardToken(
        @Body paymentRequestModel: PaymentRequestModel?
    ): Response<DataModel<PaymentRequestModel>>

    @POST("api/v1/payments")
    suspend fun charge(
        @Body paymentRequestModel: PaymentRequestModel?
    ): Response<DataModel<PaymentChargeModel>>

    @FormUrlEncoded
    @POST("api/v1/payments/{id}/qr")
    suspend fun qr(
        @Path("id") orderId: Int?,
        @Field("channel") channel: String? = AppConstant.PAYMENT_CHANNEL_CASH
    ): Response<ResponseBody>

    @POST("api/v1/payments/{id}/wechat")
    suspend fun weChat(
        @Path("id") orderId: Int?
    ): Response<ResponseBody>

    @GET("api/v1/payments/{id}/trace")
    suspend fun trace(
        @Path("id") orderId: Int?
    ): Response<DataModel<MessageModel>>

    @GET("api/v1/payments/{id}/flag")
    suspend fun flag(
        @Path("id") orderId: Int?
    ): Response<DataModel<MessageModel>>

    @POST
    suspend fun paymentGateway(
        @Url url: String,
        @Body items: TreePayModel?
    ): Response<ResponseBody>
    //endregion

    //region reward
    @GET("api/v1/rewards")
    suspend fun rewards(
        @Query("q") query: String? = null,
        @Query("page") page: Int? = null
    ): Response<DataPaginationModel<MutableList<RewardModel>>>

    @GET("api/v1/rewards/{id}")
    suspend fun rewards(
        @Path("id") rewardId: Int? = null
    ): Response<DataModel<RewardModel>>

    @POST("api/v1/rewards/{id}/redeem")
    suspend fun redeem(
        @Path("id") rewardId: Int?,
        @Body items: RedeemRewardModel?
    ): Response<DataModel<MessageModel>>
    //endregion

    //region point
    @GET("api/v1/points")
    suspend fun points(@Query("status") status: Int? = null): Response<DataModel<MutableList<PointGroupModel>>>
    //endregion

    //region notification
    @GET("api/v1/notifications")
    suspend fun notifications(@Query("page") page: Int? = null): Response<DataPaginationModel<MutableList<NotifyModel>>>
    //endregion

    //region master
    @GET("api/v1/contents")
    suspend fun uiContents(): Response<DataModel<MutableList<UiContentModel>>>
    //endregion

    //region streaming
    @GET("api/v1/streaming")
    suspend fun youtubeStreaming(): Response<DataModel<YouTubeModel>>

    @GET("api/v1/video-lists")
    suspend fun youtubeHistories(): Response<DataModel<MutableList<YouTubeModel>>>

    @GET("api/v1/streaming/{id}/products")
    suspend fun productsLive(
        @Path("id") youtubeId: String?,
    ): Response<DataModel<ProductModel>>*/
    //endregion

}