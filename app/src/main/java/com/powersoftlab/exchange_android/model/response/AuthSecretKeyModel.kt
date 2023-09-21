package com.powersoftlab.exchange_android.model.response

import com.google.gson.annotations.SerializedName

data class AuthSecretKeyModel(

	@field:SerializedName("auth")
	val secretToken: String? = null,

)
