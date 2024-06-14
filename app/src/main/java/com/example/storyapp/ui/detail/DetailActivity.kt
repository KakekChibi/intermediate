package com.example.storyapp.ui.detail

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.storyapp.databinding.ActivityDetailBinding
import com.example.storyapp.utils.Constanta

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.storyName.text =
            intent.getData(Constanta.StoryDetail.UserName.name, "Name")
        Glide.with(binding.root)
            .load(intent.getData(Constanta.StoryDetail.ImageURL.name, ""))
            .into(binding.storyImage)
        binding.storyDescription.text =
            intent.getData(Constanta.StoryDetail.ContentDescription.name, "Caption")
        binding.storyUploadTime.text =
            intent.getData(Constanta.StoryDetail.UploadTime.name, "Upload time")
    }

    private fun Intent.getData(key: String, defaultValue: String = "None"): String {
        return getStringExtra(key) ?: defaultValue
    }

}