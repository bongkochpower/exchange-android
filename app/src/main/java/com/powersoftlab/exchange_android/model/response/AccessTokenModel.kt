package com.powersoftlab.exchange_android.model.response

import com.google.gson.annotations.SerializedName

data class AccessTokenModel(

	@field:SerializedName("accessToken")
	val accessToken: String? = null,

	@field:SerializedName("is_first_login")
	val isFirstLogin: Boolean? = null
)
