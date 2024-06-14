package com.example.storyapp.data.model

data class Login(
	val error: Boolean,
	val message: String,
	val loginResult: User
)
