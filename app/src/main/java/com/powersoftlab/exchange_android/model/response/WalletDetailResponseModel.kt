package com.powersoftlab.exchange_android.model.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class WalletDetailResponseModel(
    @field:SerializedName("id") var id: Int? = null,
    @field:SerializedName("wallet_id") var walletId: String? = null,
    @field:SerializedName("first_name") var firstName: String? = null,
    @field:SerializedName("last_name") var lastName: String? = null,
    @field:SerializedName("profile_image") var profileImage: String? = null,
) : Parcelable
{
    val fullName : String
        get() = "$firstName  $lastName"
}