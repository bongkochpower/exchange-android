package com.startwithn.exchange_android.model.response

import com.google.gson.annotations.SerializedName

data class UserModel(
    @field:SerializedName("firstname")
    var firstname: String? = null,

    @field:SerializedName("citizen_id")
    var citizenId: String? = null,
)
