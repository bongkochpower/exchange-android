package com.powersoftlab.exchange_android.model.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class TransferResponseModel(
    @field:SerializedName("transactionId") var transactionId: String? = null,
    @field:SerializedName("date") var date: String? = null,
) : Parcelable
{
    data class TransferSummaryModel(
        val profileImage : String? = null,
        val fullName : String? = null,
        val accountNumber : String? = null,
        val amount : String? = null,
        val currency : String? = null,
        val date: String? = null,
        val transactionId: String? = null,
    )
}