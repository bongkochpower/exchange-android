package com.powersoftlab.exchange_android.model.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddressAutoFillResponseModel(
    @field:SerializedName("subDistrict") var subDistrict: SubDistrictResponse? = null,
    @field:SerializedName("district") var district: DistrictResponse? = null,
    @field:SerializedName("province") var province: ProvinceResponse? = null,
) : Parcelable{
    @Parcelize
    data class SubDistrictResponse(
        @field:SerializedName("id") var id: Int? = null,
        @field:SerializedName("zipCode") var zipCode: String? = null,
        @field:SerializedName("nameTH") var nameTH: String? = null,
        @field:SerializedName("nameEN") var nameEN: String? = null,
        @field:SerializedName("districtId") var subDistrictId: Int? = null,
    ):Parcelable

    @Parcelize
    data class DistrictResponse(
        @field:SerializedName("id") var id: Int? = null,
        @field:SerializedName("zipCode") var zipCode: String? = null,
        @field:SerializedName("nameTH") var nameTH: String? = null,
        @field:SerializedName("nameEN") var nameEN: String? = null,
        @field:SerializedName("provinceId") var districtId: Int? = null,
    ):Parcelable

    @Parcelize
    data class ProvinceResponse(
        @field:SerializedName("id") var id: Int? = null,
        @field:SerializedName("zipCode") var zipCode: String? = null,
        @field:SerializedName("nameTH") var nameTH: String? = null,
        @field:SerializedName("nameEN") var nameEN: String? = null,
        @field:SerializedName("geographyId") var provinceId: Int? = null,
    ):Parcelable
}