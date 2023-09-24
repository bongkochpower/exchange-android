package com.powersoftlab.exchange_android.model.body

import com.google.gson.annotations.SerializedName

data class RequestNewCardRequestModel(
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
    @field:SerializedName("country") var country: String? = null
)