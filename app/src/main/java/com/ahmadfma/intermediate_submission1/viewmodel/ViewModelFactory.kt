package com.ahmadfma.intermediate_submission1.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ahmadfma.intermediate_submission1.data.repository.AuthenticationRepository
import com.ahmadfma.intermediate_submission1.data.repository.StoryRepository
import com.ahmadfma.intermediate_submission1.injection.Injection

class ViewModelFactory private constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val storyRepository: StoryRepository
    ): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthenticationViewModel::class.java)) {
            return AuthenticationViewModel(authenticationRepository) as T
        } else if(modelClass.isAssignableFrom(StoryViewModel::class.java)) {
            return StoryViewModel(storyRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory = instance ?: synchronized(this) {
            instance ?: ViewModelFactory(
                Injection.provideAuthenticationRepository(),
                Injection.provideStoryRepository(context)
            )
        }.also {
            instance = it
        }
    }
}