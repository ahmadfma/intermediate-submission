package com.ahmadfma.intermediate_submission1.data.remote

import com.ahmadfma.intermediate_submission1.data.local.UserPreferences
import com.ahmadfma.intermediate_submission1.data.model.GetStoryResponse
import com.ahmadfma.intermediate_submission1.data.model.LoginResponse
import com.ahmadfma.intermediate_submission1.data.model.MessageResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

const val REGISTER_ENDPOINT = "register"
const val LOGIN_ENDPOINT = "login"
const val STORIES_ENDPOINT = "stories"
const val NAME_FIELD = "name"
const val EMAIL_FIELD = "email"
const val PASSWORD_FIELD = "password"
const val AUTHORIZATION = "Authorization"
const val BEARER = "Bearer "
const val DESCRIPTION_PART = "description"

interface ApiService {

    @FormUrlEncoded
    @POST(REGISTER_ENDPOINT)
    suspend fun registerUser(
        @Field(NAME_FIELD) username: String,
        @Field(EMAIL_FIELD) email: String,
        @Field(PASSWORD_FIELD) password: String,
    ): Response<MessageResponse>

    @FormUrlEncoded
    @POST(LOGIN_ENDPOINT)
    suspend fun loginUser(
        @Field(EMAIL_FIELD) email: String,
        @Field(PASSWORD_FIELD) password: String
    ): Response<LoginResponse>

    @GET(STORIES_ENDPOINT)
    suspend fun getStories(
        @Header(AUTHORIZATION) token : String = BEARER + UserPreferences.user.token
    ): Response<GetStoryResponse>

    @Multipart
    @POST(STORIES_ENDPOINT)
    suspend fun addNewStories(
        @Header(AUTHORIZATION) token : String = BEARER + UserPreferences.user.token,
        @Part image: MultipartBody.Part,
        @Part(DESCRIPTION_PART) description: RequestBody,
    ): Response<MessageResponse>

}