package com.ahmadfma.intermediate_submission1.data.remote

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.ahmadfma.intermediate_submission1.data.local.StoryDatabase
import com.ahmadfma.intermediate_submission1.data.model.ListStoryItem

@OptIn(ExperimentalPagingApi::class)
class StoryRemoteMediator (private val database: StoryDatabase, private val apiService: ApiService) : RemoteMediator<Int, ListStoryItem>() {

    private companion object {
        const val TAG = "StoryRemoteMediator"
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun initialize(): InitializeAction {
        Log.d(TAG, "initialize: ")
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, ListStoryItem>): MediatorResult {
        Log.d(TAG, "load: start")
        val page = INITIAL_PAGE_INDEX
        try {
            Log.d(TAG, "load: begin")
            val responseData = apiService.getStories(page = page, size = state.config.pageSize)
            val data = responseData.body()?.listStory
            Log.d(TAG, "load: $data")
            return if(data != null) {
                val endOfPaginationReached = data.isEmpty()

                database.withTransaction {
                    if (loadType == LoadType.REFRESH) {
                        database.storyDao().deleteAll()
                    }
                    database.storyDao().insertStories(data)
                }
                Log.d(TAG, "load: $data")
                MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
            } else {
                Log.d(TAG, "load: no data")
                MediatorResult.Error(Exception("No data"))
            }
        } catch (exception: Exception) {
            Log.e(TAG, "error : $exception")
            return MediatorResult.Error(exception)
        }
    }

}