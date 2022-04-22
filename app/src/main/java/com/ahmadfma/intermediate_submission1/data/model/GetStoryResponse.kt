package com.ahmadfma.intermediate_submission1.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class GetStoryResponse(
	@field:SerializedName("listStory")
	val listStory: List<ListStoryItem>? = null,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
) : Parcelable


@Entity(tableName = "list_story")
@Parcelize
data class ListStoryItem(

	@PrimaryKey
	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("photoUrl")
	val photoUrl: String? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("lat")
	val latitude: Double? = null,

	@field:SerializedName("lon")
	val longitude: Double? = null,

) : Parcelable
