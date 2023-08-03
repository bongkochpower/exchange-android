package com.powersoftlab.exchange_android.model.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class TopUpResponse(
    @field:SerializedName("success") var success: Boolean? = null,
    @field:SerializedName("old_balance") var oldBalance: Double? = null,
    @field:SerializedName("new_balance") var newBalance: Double? = null,
) : Parcelable