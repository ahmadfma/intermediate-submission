package com.ahmadfma.intermediate_submission1.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ahmadfma.intermediate_submission1.data.model.ListStoryItem

@Dao
interface StoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStories(stories: List<ListStoryItem>)

    @Query("SELECT * FROM list_story")
    fun getStories(): PagingSource<Int, ListStoryItem>

    @Query("DELETE FROM list_story")
    suspend fun deleteAll()

}