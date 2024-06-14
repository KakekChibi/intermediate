package com.example.storyapp

import com.example.storyapp.data.model.Story

object  DataDummy {

    fun generateDummyStoryResponse(): List<Story> {
        val items: MutableList<Story> = arrayListOf()
        for (i in 0..100) {
            val story = Story(
                photoUrl = "https://example.com/photo$i.jpg",
                createdAt = "2021-09-15T09:30:00Z",
                name = "author $i",
                description = "quote $i",
                lon = 100.0 + i,
                id = i.toString(),
                lat = -100.0 - i
            )
            items.add(story)
        }
        return items
    }
}
