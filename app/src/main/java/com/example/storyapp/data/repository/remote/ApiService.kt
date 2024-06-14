package com.example.storyapp.data.repository.remote

import com.example.storyapp.data.model.Login
import com.example.storyapp.data.model.Register
import com.example.storyapp.data.model.StoryList
import com.example.storyapp.data.model.StoryUpload
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {

    @POST("login")
    @FormUrlEncoded
    suspend fun doLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): Login

    @POST("register")
    @FormUrlEncoded
    suspend fun doRegister(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Register

//    @GET("stories")
//    fun getStoryList(
//        @Header("Authorization") token: String
//    ): Call<StoryList>

    @Multipart
    @POST("stories")
    suspend fun doUploadImage(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Header("Authorization") auth: String
    ): StoryUpload

    @GET("stories")
   suspend fun getStoriesWithLocation(
        @Header("Authorization") token: String,
        @Query("location") location: Int
    ): StoryList

    @GET("stories")
    suspend fun getStories(
        @Header("Authorization") token: String,
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 20
    ): StoryList
}