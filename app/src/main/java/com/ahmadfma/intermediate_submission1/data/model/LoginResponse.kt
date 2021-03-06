package com.ahmadfma.intermediate_submission1.data.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("loginResult")
	val loginResult: UserData? = null,

)

data class UserData(

	@field:SerializedName("name")
	var name: String? = null,

	@field:SerializedName("token")
	var token: String? = null
)
