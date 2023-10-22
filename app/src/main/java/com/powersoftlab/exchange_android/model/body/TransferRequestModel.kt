package com.powersoftlab.exchange_android.model.body

import com.google.gson.annotations.SerializedName

data class TransferRequestModel(
    @SerializedName("currency_id")
    var currencyId: Int,
    @SerializedName("amount")
    var amount : Double,
    @SerializedName("wallet_id")
    var walletId: String
)