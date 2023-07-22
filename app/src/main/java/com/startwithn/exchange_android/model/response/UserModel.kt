package com.startwithn.exchange_android.model.response

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class UserModel(
    @field:SerializedName("address")
    var address: String?,
    @field:SerializedName("birth_date")
    var birthDate: String?,
    @field:SerializedName("customer_balances")
    var customerBalances: MutableList<CustomerBalance>?,
    @field:SerializedName("district")
    var district: String?,
    @field:SerializedName("district_id")
    var districtId: Int?,
    @field:SerializedName("email")
    var email: String?,
    @field:SerializedName("first_name")
    var firstName: String?,
    @field:SerializedName("house_no")
    var houseNo: String?,
    @field:SerializedName("id")
    var id: Int?,
    @field:SerializedName("id_card_image")
    var idCardImage: String?,
    @field:SerializedName("profile_image")
    var profileImage: String?,
    @field:SerializedName("is_active")
    var isActive: Boolean?,
    @field:SerializedName("is_consent")
    var isConsent: Boolean?,
    @field:SerializedName("last_name")
    var lastName: String?,
    @field:SerializedName("moo")
    var moo: String?,
    @field:SerializedName("post_code")
    var postCode: String?,
    @field:SerializedName("provine")
    var provine: String?,
    @field:SerializedName("provine_id")
    var provineId: Int?,
    @field:SerializedName("road")
    var road: String?,
    @field:SerializedName("soi")
    var soi: String?,
    @field:SerializedName("sub_distict")
    var subDistict: String?,
    @field:SerializedName("sub_distict_id")
    var subDistictId: Int?,
    @field:SerializedName("tel")
    var tel: String?,
    @field:SerializedName("village")
    var village: String?
) {
    @Parcelize
    data class CustomerBalance(
        @field:SerializedName("currency_id")
        var id: Int?,
        @field:SerializedName("balance")
        var balance: Double?,
        @field:SerializedName("currency_name")
        var currencyName: String?,
        @field:SerializedName("label")
        var label: String?,

        @Transient
        var isSelected : Boolean = false
    ) : Parcelable
}

