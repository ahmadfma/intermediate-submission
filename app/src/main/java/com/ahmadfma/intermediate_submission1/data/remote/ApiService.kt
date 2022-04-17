package com.ahmadfma.intermediate_submission1.data.remote

import com.ahmadfma.intermediate_submission1.data.local.UserPreferences
import com.ahmadfma.intermediate_submission1.data.model.GetStoryResponse
import com.ahmadfma.intermediate_submission1.data.model.LoginResponse
import com.ahmadfma.intermediate_submission1.data.model.MessageResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*


interface ApiService {

    @FormUrlEncoded
    @POST("register")
    suspend fun registerUser(
        @Field("name") username: String,
        @Field("email") email: String,
        @Field("password") password: String,
    ): Response<MessageResponse>

    @FormUrlEncoded
    @POST("login")
    suspend fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<LoginResponse>

    @GET("stories")
    suspend fun getStories(
        @Header("Authorization") token : String = "Bearer " + UserPreferences.user.token
    ): Response<GetStoryResponse>

    @Multipart
    @POST("stories")
    suspend fun addNewStories(
        @Header("Authorization") token : String = "Bearer " + UserPreferences.user.token,
        @Part image: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): Response<MessageResponse>

}