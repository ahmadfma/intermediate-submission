package com.ahmadfma.intermediate_submission1.data

import com.ahmadfma.intermediate_submission1.data.model.GetStoryResponse
import com.ahmadfma.intermediate_submission1.data.model.LoginResponse
import com.ahmadfma.intermediate_submission1.data.model.MessageResponse
import com.ahmadfma.intermediate_submission1.data.remote.ApiService
import com.ahmadfma.intermediate_submission1.helper.Validator
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.lang.Exception

class FakeApiService : ApiService {

    companion object {
        const val SUCCESS = "Success"
        const val ERROR_EMAIL = "Invalid Email"
        const val ERROR_PASSWORD = "Invalid Password"
    }

    override suspend fun registerUser(
        username: String,
        email: String,
        password: String
    ): Response<MessageResponse> {
        if (Validator.isEmailValid(email)) {
            if (password.length >= 6) {
                return Response.success(
                    MessageResponse(
                        error = false,
                        message = SUCCESS
                    )
                )
            } else {
                throw Exception(ERROR_PASSWORD)
            }
        } else {
            throw Exception(ERROR_EMAIL)
        }
    }

    override suspend fun loginUser(email: String, password: String): Response<LoginResponse> {
        if (Validator.isEmailValid(email)) {
            if (password.length >= 6) {
                return Response.success(
                    LoginResponse(
                        false,
                        SUCCESS
                    )
                )
            } else {
                throw Exception(ERROR_PASSWORD)
            }
        } else {
            throw Exception(ERROR_EMAIL)
        }
    }

    override suspend fun getStories(
        token: String,
        page: Int,
        size: Int,
        location: Int
    ): Response<GetStoryResponse> {
        return Response.success(
            GetStoryResponse(
                DataDummy.generateDummyStories(),
                false,
                SUCCESS
            )
        )
    }

    override suspend fun addNewStories(
        token: String,
        image: MultipartBody.Part,
        description: RequestBody,
        latitude: RequestBody?,
        longitude: RequestBody?
    ): Response<MessageResponse> {
        return Response.success(
            MessageResponse(
                false,
                SUCCESS
            )
        )
    }


}