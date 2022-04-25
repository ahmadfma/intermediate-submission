package com.ahmadfma.intermediate_submission1.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = RemoteKeys.TABLE_NAME)
data class RemoteKeys(
    @PrimaryKey val id: String,
    val prevKey: Int?,
    val nextKey: Int?
) {
    companion object {
        const val TABLE_NAME = "remote_keys"
    }
}