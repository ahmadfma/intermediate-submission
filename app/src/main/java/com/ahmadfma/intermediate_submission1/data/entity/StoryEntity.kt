package com.ahmadfma.intermediate_submission1.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

const val STORY_TABLE_NAME = "list_story"
@Entity(tableName = STORY_TABLE_NAME)
data class StoryEntity(
    @PrimaryKey
    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("photoUrl")
    val photo_url: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("createdAt")
    val createdAt: String? = null
)