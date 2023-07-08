package com.startwithn.exchange_android.model.response

import com.google.gson.annotations.SerializedName

data class MessageModel(
    @SerializedName("message")
    var message: String? = null,
    @SerializedName("details")
    var details: ErrorDetailsModel? = null
) {
    fun getErrorMessage(): String? = if (details != null) {
        details?.body?.first()?.message
    } else {
        message
    }
}

data class ErrorDetailsModel(
    @SerializedName("body")
    var body: List<ErrorBodyModel>? = null
)

data class ErrorBodyModel(
    @SerializedName("message")
    var message: String? = null
)
