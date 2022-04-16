package com.ahmadfma.intermediate_submission1.viewmodel

import androidx.lifecycle.ViewModel
import com.ahmadfma.intermediate_submission1.data.repository.StoryRepository

class StoryViewModel(private val storyRepository: StoryRepository): ViewModel() {

    fun getStories() = storyRepository.getStories()

}