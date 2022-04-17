package com.ahmadfma.intermediate_submission1.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.ahmadfma.intermediate_submission1.data.remote.ApiService
import com.ahmadfma.intermediate_submission1.data.Result
import com.ahmadfma.intermediate_submission1.data.model.GetStoryResponse
import com.ahmadfma.intermediate_submission1.data.model.MessageResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

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

    fun addStories(image: MultipartBody.Part, description: RequestBody) : LiveData<Result<MessageResponse?>> = liveData {
        emit(Result.Loading)
        try {
            val returnValue = MutableLiveData<Result<MessageResponse?>>()
            val response = apiService.addNewStories(image = image, description = description)
            returnValue.value = Result.Success(response.body())
            emitSource(returnValue)
        }
        catch (e: java.lang.Exception) {
            emit(Result.Error(e.toString()))
        }
    }

}