package com.powersoftlab.exchange_android.model.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CardsModel(
    @field:SerializedName("cardNo")
    var cardNo: String?,
    @field:SerializedName("validDate")
    var validDate: String?,
    @field:SerializedName("cardName")
    var cardName: String?,
) : Parcelable