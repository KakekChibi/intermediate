package com.example.storyapp.data.model

import com.google.gson.annotations.SerializedName

data class StoryUpload(

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)