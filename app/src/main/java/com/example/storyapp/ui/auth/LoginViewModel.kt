package com.example.storyapp.ui.auth

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.model.Login
import com.example.storyapp.data.repository.remote.UserModel
import com.example.storyapp.data.repository.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    val loginResult = MutableLiveData<Login>()
    val error = MutableLiveData<String>()

    fun login(email: String, password: String, onSuccess: (UserModel) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = repository.login(email, password)
                if (!response.error) {
                    val user = UserModel(
                        email = email,
                        token = response.loginResult.token,
                        isLogin = true,
                        userId = response.loginResult.userId,
                        name = response.loginResult.name
                    )
//                    saveSession(user)
//                    loginResult.postValue(response)
                    saveSession(user)
                    onSuccess(user)
                } else {
                    onError(response.message)
                }
            } catch (e: Exception) {
                Log.e(TAG, "onFailure Call: ${e.message}")
                onError(e.message ?: "Unknown error")
            }
        }
    }

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    companion object {
        private const val TAG = "LoginViewModel"
    }
}
