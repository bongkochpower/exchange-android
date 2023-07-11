package com.startwithn.exchange_android.model.body

import com.google.gson.annotations.SerializedName

data class LoginRequestModel(
    @SerializedName("mobile")
    var mobile: String,
    @SerializedName("password")
    var password: String
)
