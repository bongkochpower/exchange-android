package com.startwithn.exchange_android.model.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.startwithn.exchange_android.common.enum.TransactionStatusEnum
import kotlinx.parcelize.Parcelize

@Parcelize
data class TransactionsModel(
    @field:SerializedName("balance")
    var balance: Double?,
    @field:SerializedName("before_balance")
    var beforeBalance: Double?,
    @field:SerializedName("created_at")
    var createdAt: String?,
    @field:SerializedName("currency_change_to")
    var currencyChangeTo: String?,
    @field:SerializedName("currency_text")
    var currencyText: String?,
    @field:SerializedName("message")
    var message: String?,
    @field:SerializedName("status")
    var status: String?,
    @field:SerializedName("type")
    var type: String?,
    @field:SerializedName("type_text")
    var typeText: String?,
    @field:SerializedName("value")
    var value: Double?
) : Parcelable