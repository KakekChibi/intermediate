package com.example.storyapp.data.repository


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.storyapp.data.model.Login
import com.example.storyapp.data.model.Register
import com.example.storyapp.data.model.Story
import com.example.storyapp.data.model.StoryUpload
import com.example.storyapp.data.repository.remote.ApiService
import com.example.storyapp.data.repository.remote.ResultState
import com.example.storyapp.data.repository.remote.StoryPagingSource
import com.example.storyapp.data.repository.remote.UserModel
import com.example.storyapp.data.repository.remote.UserPreference
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class UserRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {

    suspend fun register(name: String, email: String, password: String): Register {
        return apiService.doRegister(name, email, password)
    }

    suspend fun login(email: String, password: String): Login {
        return apiService.doLogin(email, password)
    }

    suspend fun getSession(): UserModel {
        return userPreference.getUser().first()
    }

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    suspend fun logout() {
        userPreference.logout()
    }


    suspend fun getStoriesWithLocation(): List<Story> {
        val user = runBlocking { userPreference.getUser().first() }
        val tokenConcatenate = "Bearer ${user.token}"
        Log.d("UserRepository", "Fetching stories with location with token: $tokenConcatenate")

        return try {
            val response = apiService.getStoriesWithLocation(tokenConcatenate, 1)
            response.listStory
        } catch (e: Exception) {
            Log.e("UserRepository", "Error: ${e.message}", e)
            emptyList()
        }
    }

    fun getPagedStories(): LiveData<PagingData<Story>> {
        val user = runBlocking { userPreference.getUser().first() }
        val tokenConcatenate = "Bearer ${user.token}"
        Log.d("UserRepository", "Fetching stories with token: $tokenConcatenate")
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService, tokenConcatenate)
            }
        ).liveData
    }


    fun uploadNewStory(imageFile: File, description: String, token: String) = liveData {
        emit(ResultState.Loading)
        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )
        try {
            val successResponse = apiService.doUploadImage(multipartBody, requestBody, "Bearer $token")
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, StoryUpload::class.java)
            emit(errorResponse.message?.let { ResultState.Error(it) })
        }
    }


    companion object {
        @Volatile
        private var INSTANCE: UserRepository? = null

        fun getInstance(apiService: ApiService, userPreference: UserPreference): UserRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = UserRepository(apiService, userPreference)
                INSTANCE = instance
                instance
            }
        }
    }
}


