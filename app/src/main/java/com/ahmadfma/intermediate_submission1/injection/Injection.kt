package com.ahmadfma.intermediate_submission1.injection

import android.content.Context
import com.ahmadfma.intermediate_submission1.data.local.StoryDatabase
import com.ahmadfma.intermediate_submission1.data.remote.RetrofitClient
import com.ahmadfma.intermediate_submission1.data.repository.AuthenticationRepository
import com.ahmadfma.intermediate_submission1.data.repository.StoryRepository

object Injection {

    fun provideAuthenticationRepository(): AuthenticationRepository {
        val apiService = RetrofitClient.getInstance()
        return AuthenticationRepository(apiService)
    }

    fun provideStoryRepository(context: Context): StoryRepository {
        val apiService = RetrofitClient.getInstance()
        val storyDatabase = StoryDatabase.getDatabase(context)
        return StoryRepository(storyDatabase, apiService)
    }

}