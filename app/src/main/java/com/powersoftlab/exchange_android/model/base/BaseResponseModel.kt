package com.powersoftlab.exchange_android.model.base

import com.google.gson.annotations.SerializedName

data class BaseResponseModel<T>(
    @SerializedName("data")
    var data: T? = null,

    @SerializedName("page")
    var page: Int? = null,

    @SerializedName("limit")
    var limit: Int? = null,

    @SerializedName("total")
    var total: Int? = null,
) {
    private fun isCanNextPage(): Boolean =
        if (total != null && limit != null) {
            when {
                total!! >= limit!! -> true
                else -> false
            }
        } else {
            false
        }

    fun getNextPage(): Int? =
        if (isCanNextPage()) {
            page?.plus(1)
        } else {
            null
        }
}