package com.powersoftlab.exchange_android.model.body

import com.google.gson.annotations.SerializedName

data class LoginRequestModel(
    @SerializedName("username")
    var username: String,
    @SerializedName("password")
    var password: String
)
