package com.example.storyapp.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.storyapp.data.repository.UserRepository
import com.example.storyapp.data.repository.remote.ApiConfig
import com.example.storyapp.data.repository.remote.UserPreference


object Constanta {

   private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    fun provideUserRepository(context: Context): UserRepository {
        val dataStore = context.dataStore
        val userPreference = UserPreference.getInstance(dataStore)
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(apiService, userPreference)
    }



    enum class StoryDetail {
        UserName, ImageURL, ContentDescription, UploadTime,
    }


    val emailPattern = Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")

}