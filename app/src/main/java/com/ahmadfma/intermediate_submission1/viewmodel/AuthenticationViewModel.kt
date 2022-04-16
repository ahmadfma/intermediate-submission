package com.ahmadfma.intermediate_submission1.viewmodel

import androidx.lifecycle.ViewModel
import com.ahmadfma.intermediate_submission1.data.repository.AuthenticationRepository

class AuthenticationViewModel(private val authenticationRepository: AuthenticationRepository): ViewModel() {

    fun registerUser(username: String, email: String, password: String) = authenticationRepository.register(username, email, password)

}