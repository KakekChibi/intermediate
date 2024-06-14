package com.example.storyapp.ui.dashboard.story

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.storyapp.data.repository.UserRepository
import java.io.File

class NewStoryViewModel(private val userRepository: UserRepository) : ViewModel() {

//    private val _isSuccessUploadStory = MutableLiveData<Boolean>()
//    val isSuccessUploadStory: LiveData<Boolean> get() = _isSuccessUploadStory
//
//    private val _error = MutableLiveData<String>()
//    val error: LiveData<String> get() = _error
//
//    val loading = MutableLiveData<Boolean>()

    fun uploadNewStory(file: File, description: String, token: String) = userRepository.uploadNewStory(file, description, token)

    fun getSession() = liveData {
        val user = userRepository.getSession()
        emit(user)
    }
}
