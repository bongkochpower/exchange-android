package com.startwithn.exchange_android.model.response

import com.google.gson.annotations.SerializedName

data class RegisterResponseModel(
    @field:SerializedName("id") var id: Int? = null,
    @field:SerializedName("is_activate") var isActivate: Boolean? = null,
    @field:SerializedName("first_name") var firstname: String? = null,
    @field:SerializedName("last_name") var lastname: String? = null,
    @field:SerializedName("birth_date") var birtDate: String? = null, //2023-01-01
    @field:SerializedName("tel") var tel: String? = null,
    @field:SerializedName("email") var email: String? = null,
    @field:SerializedName("password") var password: String? = null,
    @field:SerializedName("id_card_image") var idCardImagePath: String? = null,
    @field:SerializedName("address") var address: String? = null,
    @field:SerializedName("post_code") var postCode: String? = null,
    @field:SerializedName("moo") var moo: String? = null,
    @field:SerializedName("soi") var soi: String? = null,
    @field:SerializedName("house_no") var houseNo: String? = null,
    @field:SerializedName("village") var village: String? = null,
    @field:SerializedName("road") var road: String? = null,
    @field:SerializedName("district_id") var districtId: Int? = null,
    @field:SerializedName("district") var district: String? = null,
    @field:SerializedName("sub_distict_id") var subDistrictId: Int? = null,
    @field:SerializedName("sub_distict") var subDistrict: String? = null,
    @field:SerializedName("provine") var province: String? = null,
    @field:SerializedName("provine_id") var provinceId: Int? = null,
    @field:SerializedName("is_consent") var isConsent: Boolean? = null,
)