package com.powersoftlab.exchange_android.model.body

import com.google.gson.annotations.SerializedName

data class WithdrawRequestModel(
    @SerializedName("currency_id")
    var currencyId: Int,
    @SerializedName("value")
    var amount : Double,
    @SerializedName("shop_id")
    var shopId: Int
)