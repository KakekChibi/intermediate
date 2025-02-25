package com.example.storyapp.data.model

import com.google.gson.annotations.SerializedName

data class StoryList(
	@field:SerializedName("listStory")
	val listStory: List<Story> = emptyList(),

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null

)
