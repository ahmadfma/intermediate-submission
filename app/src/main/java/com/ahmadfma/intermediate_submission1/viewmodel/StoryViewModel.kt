package com.ahmadfma.intermediate_submission1.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ahmadfma.intermediate_submission1.data.model.ListStoryItem
import com.ahmadfma.intermediate_submission1.data.repository.StoryRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryViewModel(private val storyRepository: StoryRepository): ViewModel() {

    val stories: LiveData<PagingData<ListStoryItem>> = storyRepository.getStoriesWithPaging().cachedIn(viewModelScope)

    @JvmName("getStories1")
    fun getStories() = storyRepository.getStories()

    fun addNewStories(image: MultipartBody.Part, description: RequestBody) = storyRepository.addStories(image, description)
}