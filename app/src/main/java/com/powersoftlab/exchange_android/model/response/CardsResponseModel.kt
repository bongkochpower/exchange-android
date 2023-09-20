package com.powersoftlab.exchange_android.model.response

import com.google.gson.annotations.SerializedName

data class CardsResponseModel(
    @field:SerializedName("uuid") var cardNo: String? = null,
    @field:SerializedName("id") var id: Int? = null,
    @field:SerializedName("validDate") var validDate: String? = null,
    @field:SerializedName("cardName") var cardName: String? = null,
    @field:SerializedName("yourName") var yourName: String? = null,
)