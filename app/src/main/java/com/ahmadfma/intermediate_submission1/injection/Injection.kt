package com.ahmadfma.intermediate_submission1.injection

import com.ahmadfma.intermediate_submission1.data.remote.RetrofitClient
import com.ahmadfma.intermediate_submission1.data.repository.AuthenticationRepository
import com.ahmadfma.intermediate_submission1.data.repository.StoryRepository

object Injection {

    fun provideAuthenticationRepository(): AuthenticationRepository {
        val apiService = RetrofitClient.getInstance()
        return AuthenticationRepository(apiService)
    }

    fun provideStoryRepository(): StoryRepository {
        val apiService = RetrofitClient.getInstance()
        return StoryRepository(apiService)
    }

}