package com.example.storyapp.ui.dashboard.home

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapp.data.model.Story
import com.example.storyapp.databinding.RvStoryBinding
import com.example.storyapp.ui.detail.DetailActivity
import com.example.storyapp.utils.Constanta

class HomeAdapter : PagingDataAdapter<Story, HomeAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RvStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val story = getItem(position)
        Log.d("HomeAdapter", "Binding story at position $position: $story")
        if (story != null) {
            holder.bind(story)
        }
    }

    inner class ViewHolder(private val binding: RvStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
//        @SuppressLint("SetTextI18n")
        fun bind(story: Story) {
            with(binding) {
                storyName.text = story.name
//                storyUploadTime.text =
//                    " ${itemView.context.getString(R.string.const_text_uploaded)} ${
//                        Helper.getTimelineUpload(
//                            itemView.context,
//                            story.createdAt
//                        )
//                    }"
                Glide.with(itemView)
                    .load(story.photoUrl)
                    .into(storyImage)
                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, DetailActivity::class.java)
                    intent.putExtra(Constanta.StoryDetail.UserName.name, story.name)
                    intent.putExtra(Constanta.StoryDetail.ImageURL.name, story.photoUrl)
                    intent.putExtra(
                        Constanta.StoryDetail.ContentDescription.name,
                        story.description
                    )
//                    intent.putExtra(
//                        Constanta.StoryDetail.UploadTime.name,
//                        "${itemView.context.getString(R.string.const_text_uploaded)} ${
//                            itemView.context.getString(
//                                R.string.const_text_time_on
//                            )
//                        }"
////                                + " ${Helper.getUploadStoryTime(story.createdAt)}"
//
//                    )
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            itemView.context as Activity,
                            androidx.core.util.Pair(storyImage, "story_image"),
                            androidx.core.util.Pair(storyName, "user_name"),
                            androidx.core.util.Pair(defaultAvatar, "user_avatar"),
                        )
                    itemView.context.startActivity(intent, optionsCompat.toBundle())
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem.id == newItem.id
            }
            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }
        }
    }
}

