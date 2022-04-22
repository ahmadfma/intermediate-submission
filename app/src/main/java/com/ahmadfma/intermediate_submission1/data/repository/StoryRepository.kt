package com.ahmadfma.intermediate_submission1.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.paging.*
import com.ahmadfma.intermediate_submission1.data.remote.ApiService
import com.ahmadfma.intermediate_submission1.data.Result
import com.ahmadfma.intermediate_submission1.data.local.StoryDatabase
import com.ahmadfma.intermediate_submission1.data.model.GetStoryResponse
import com.ahmadfma.intermediate_submission1.data.model.ListStoryItem
import com.ahmadfma.intermediate_submission1.data.model.MessageResponse
import com.ahmadfma.intermediate_submission1.data.remote.STORY_CONSIDERING_TO_LOCATION
import com.ahmadfma.intermediate_submission1.data.remote.StoryRemoteMediator
import com.google.gson.Gson
import kotlinx.coroutines.delay
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepository(private val storyDatabase: StoryDatabase, private val apiService: ApiService) {

    fun getStoriesWithLocation() : LiveData<Result<GetStoryResponse?>> = liveData {
        emit(Result.Loading)
        try {
            val returnValue = MutableLiveData<Result<GetStoryResponse?>>()
            val response = apiService.getStories(page = 1, size = 15, location = STORY_CONSIDERING_TO_LOCATION)
            if(response.isSuccessful) {
                returnValue.value = Result.Success(response.body())
                emitSource(returnValue)
            } else {
                val error = Gson().fromJson(response.errorBody()?.stringSuspending(), GetStoryResponse::class.java)
                response.errorBody()?.close()
                returnValue.value = Result.Success(error)
                emitSource(returnValue)
            }
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
            if(response.isSuccessful) {
                returnValue.value = Result.Success(response.body())
                delay(1500)
                emitSource(returnValue)
            } else {
                val error = Gson().fromJson(response.errorBody()?.stringSuspending(), MessageResponse::class.java)
                response.errorBody()?.close()
                returnValue.value = Result.Success(error)
                emitSource(returnValue)
            }
        }
        catch (e: java.lang.Exception) {
            emit(Result.Error(e.toString()))
        }
    }

    fun getStoriesWithPaging(): LiveData<PagingData<ListStoryItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService),
            pagingSourceFactory = {
                storyDatabase.storyDao().getStories()
            }
        ).liveData
    }

}