package com.example.storyapp.data.repository.remote

data class UserModel(
    val email: String,
    val token: String,
    val userId: String = "",
    val name: String = "",
    val isLogin: Boolean

)