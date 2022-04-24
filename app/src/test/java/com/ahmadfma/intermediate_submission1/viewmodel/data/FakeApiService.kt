package com.ahmadfma.intermediate_submission1.viewmodel.data

import com.ahmadfma.intermediate_submission1.data.model.GetStoryResponse
import com.ahmadfma.intermediate_submission1.data.model.LoginResponse
import com.ahmadfma.intermediate_submission1.data.model.MessageResponse
import com.ahmadfma.intermediate_submission1.data.remote.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import com.ahmadfma.intermediate_submission1.helper.Validator

class FakeApiService : ApiService {

    private val dummyResponse = DataDummy.generateDummyStories()

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
                        message = "Register Success"
                    )
                )
            } else {
                throw Exception("Invalid Password")
            }
        } else {
            throw Exception("Invalid Email")
        }
    }

    override suspend fun loginUser(email: String, password: String): Response<LoginResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getStories(
        token: String,
        page: Int,
        size: Int,
        location: Int
    ): Response<GetStoryResponse> {
        return Response.success(
            GetStoryResponse(
                error = false,
                message = "get dummy data",
                listStory = dummyResponse
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
        TODO("Not yet implemented")
    }


}