package com.ahmadfma.intermediate_submission1.viewmodel

import androidx.lifecycle.ViewModel
import com.ahmadfma.intermediate_submission1.data.repository.StoryRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryViewModel(private val storyRepository: StoryRepository): ViewModel() {

    fun getStories() = storyRepository.getStories()

    fun addNewStories(image: MultipartBody.Part, description: RequestBody) = storyRepository.addStories(image, description)
}