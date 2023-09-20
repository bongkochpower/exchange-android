package com.powersoftlab.exchange_android.model.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class WithdrawResponseModel(
    @field:SerializedName("success") var success: Boolean? = null,
    @field:SerializedName("transactionDate") var transactionDate: String? = null,
    @field:SerializedName("amount") var amount: String? = null,
    @field:SerializedName("country") var country: String? = null,
    @field:SerializedName("shop") var shop: String? = null,
) : Parcelable