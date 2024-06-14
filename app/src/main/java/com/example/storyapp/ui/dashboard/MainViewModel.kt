package com.example.storyapp.ui.dashboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyapp.data.model.Story
import com.example.storyapp.data.repository.UserRepository
import com.example.storyapp.data.repository.remote.UserModel
import kotlinx.coroutines.launch

class MainViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _userSession = MutableLiveData<UserModel?>()

    val stories: LiveData<PagingData<Story>> = userRepository.getPagedStories().cachedIn(viewModelScope)

    fun fetchUserSession() {
        viewModelScope.launch {
            try {
                val user = userRepository.getSession()
                _userSession.value = user
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error fetching user session", e)
                _userSession.value = null
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }

}