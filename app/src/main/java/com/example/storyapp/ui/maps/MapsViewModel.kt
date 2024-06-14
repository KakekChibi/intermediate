package com.example.storyapp.ui.maps

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.model.Story
import com.example.storyapp.data.repository.remote.ResultState
import com.example.storyapp.data.repository.UserRepository
import kotlinx.coroutines.launch

class MapsViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _storyList = MutableLiveData<ResultState<List<Story>>>()
    val storyList: LiveData<ResultState<List<Story>>> get() = _storyList

    fun fetchStoriesWithLocation() {
        viewModelScope.launch {
            _storyList.postValue(ResultState.Loading)
            try {
                val stories = userRepository.getStoriesWithLocation()
                if (stories.isNotEmpty()) {
                    _storyList.postValue(ResultState.Success(stories))
                } else {
                    _storyList.postValue(ResultState.Error("No stories found with location"))
                }
            } catch (e: Exception) {
                Log.e("MapsViewModel", "Error fetching stories with location", e)
                _storyList.postValue(ResultState.Error(e.message.toString()))
            }
        }
    }
}
