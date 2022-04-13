package com.ahmadfma.intermediate_submission1.data.model

import com.google.gson.annotations.SerializedName

data class MessageResponse(

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)
