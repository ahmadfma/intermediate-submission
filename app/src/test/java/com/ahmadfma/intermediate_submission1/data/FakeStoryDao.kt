package com.ahmadfma.intermediate_submission1.data

import androidx.paging.PagingSource
import com.ahmadfma.intermediate_submission1.data.local.StoryDao
import com.ahmadfma.intermediate_submission1.data.model.ListStoryItem
import com.ahmadfma.intermediate_submission1.utils.PagingSourceUtils

class FakeStoryDao: StoryDao {

    private var fakeStories = mutableListOf<ListStoryItem>()

    override suspend fun insertStories(stories: List<ListStoryItem>) {
        fakeStories.addAll(stories)
    }

    override fun getStories(): PagingSource<Int, ListStoryItem> {
        return PagingSourceUtils(fakeStories)
    }

    override suspend fun deleteAll() {
        fakeStories.clear()
    }

}