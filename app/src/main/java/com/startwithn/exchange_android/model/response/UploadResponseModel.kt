package com.startwithn.exchange_android.model.response

import com.google.gson.annotations.SerializedName

data class UploadResponseModel(
    @field:SerializedName("filename") var fileName: String? = null,
)