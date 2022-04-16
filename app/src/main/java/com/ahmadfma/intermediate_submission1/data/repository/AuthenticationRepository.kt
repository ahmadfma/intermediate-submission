package com.ahmadfma.intermediate_submission1.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.ahmadfma.intermediate_submission1.data.model.MessageResponse
import com.ahmadfma.intermediate_submission1.data.remote.ApiService
import com.ahmadfma.intermediate_submission1.data.Result
import com.ahmadfma.intermediate_submission1.data.model.LoginResponse
import java.lang.Exception

class AuthenticationRepository(private val apiService: ApiService) {

    fun register(username: String, email: String, password: String): LiveData<Result<MessageResponse?>> = liveData {
        emit(Result.Loading)
        try {
            val returnValue = MutableLiveData<Result<MessageResponse?>>()
            val response = apiService.registerUser(username, email, password)
            returnValue.value = Result.Success(response.body())
            emitSource(returnValue)
        }
        catch (e: Exception) {
            emit(Result.Error(e.toString()))
        }
    }

    fun login(email: String, password: String): LiveData<Result<LoginResponse?>> = liveData {
        emit(Result.Loading)
        try {
            val returnValue = MutableLiveData<Result<LoginResponse?>>()
            val response = apiService.loginUser(email, password)
            returnValue.value = Result.Success(response.body())
            emitSource(returnValue)
        }
        catch (e: Exception) {
            emit(Result.Error(e.toString()))
        }
    }


}