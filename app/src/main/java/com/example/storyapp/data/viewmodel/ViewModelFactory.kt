package com.example.storyapp.data.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.data.repository.UserRepository
import com.example.storyapp.ui.auth.LoginViewModel
import com.example.storyapp.ui.auth.RegisterViewModel
import com.example.storyapp.ui.dashboard.MainViewModel
import com.example.storyapp.ui.dashboard.story.NewStoryViewModel
import com.example.storyapp.ui.maps.MapsViewModel
import com.example.storyapp.utils.Constanta

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    private val userRepository: UserRepository,
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) ->
                MainViewModel(userRepository) as T

            modelClass.isAssignableFrom(LoginViewModel::class.java) ->
                LoginViewModel(userRepository) as T

            modelClass.isAssignableFrom(RegisterViewModel::class.java) ->
                RegisterViewModel(userRepository) as T

            modelClass.isAssignableFrom(NewStoryViewModel::class.java) ->
                NewStoryViewModel(userRepository) as T

            modelClass.isAssignableFrom(MapsViewModel::class.java) ->
                MapsViewModel(userRepository) as T

            else -> throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory {
            return INSTANCE ?: synchronized(this) {
                val userRepository = Constanta.provideUserRepository(context)
                INSTANCE ?: ViewModelFactory(userRepository).also { INSTANCE = it }
            }
        }
    }
}
