package com.powersoftlab.exchange_android.model.body

import com.google.gson.annotations.SerializedName

data class ExchangeRequestModel(
    @SerializedName("currency_main")
    var currencyFormId: Int,
    @SerializedName("currency_to")
    var currencyToId : Int,
    @SerializedName("amount")
    var amount: Double
)