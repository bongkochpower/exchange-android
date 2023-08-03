package com.powersoftlab.exchange_android.model.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ExchangeCalculateResponse(
    @field:SerializedName("change_value") var value: Double? = null,
) : Parcelable