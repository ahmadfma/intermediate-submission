package com.ahmadfma.intermediate_submission1.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.ahmadfma.intermediate_submission1.data.model.ListStoryItem
import com.ahmadfma.intermediate_submission1.data.remote.ApiService
import com.ahmadfma.intermediate_submission1.data.Result
import com.ahmadfma.intermediate_submission1.data.model.GetStoryResponse

class StoryRepository(private val apiService: ApiService) {

    fun getStories() : LiveData<Result<GetStoryResponse?>> = liveData {
        emit(Result.Loading)
        try {
            val returnValue = MutableLiveData<Result<GetStoryResponse?>>()
            val response = apiService.getStories()
            returnValue.value = Result.Success(response.body())
            emitSource(returnValue)
        }
        catch (e: Exception) {
            emit(Result.Error(e.toString()))
        }
    }

}