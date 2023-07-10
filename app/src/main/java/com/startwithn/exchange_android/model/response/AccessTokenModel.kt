package com.startwithn.exchange_android.model.response

import com.google.gson.annotations.SerializedName

data class AccessTokenModel(

	@field:SerializedName("accessToken")
	val accessToken: String? = null
)
