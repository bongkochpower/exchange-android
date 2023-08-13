package com.powersoftlab.exchange_android.model.body

import com.google.gson.annotations.SerializedName

data class LoginSocialRequestModel(
    @SerializedName("social")
    var social: String,
    @SerializedName("access_token")
    var accessToken: String
)
