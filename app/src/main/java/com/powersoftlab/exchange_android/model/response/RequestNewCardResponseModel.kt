package com.powersoftlab.exchange_android.model.response

import com.google.gson.annotations.SerializedName

data class RequestNewCardResponseModel(
    @field:SerializedName("id") var id: Int? = null,
    @field:SerializedName("is_active") var isActivate: Boolean? = null,
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
    @field:SerializedName("customer_id") var customerId: Int? = null,

)

/*{
    "": true,
    "id": 3,
    "post_code": "24110",
    "moo": "3",
    "soi": "-",
    "house_no": "14",
    "village": "-",
    "road": "-",
    "district": "-",
    "district_id": 1,
    "sub_distict": "-",
    "sub_distict_id": 1,
    "provine": "-",
    "provine_id": 1,
    "created_by": "undefined undefined",
    "customer_id": 100
}*/