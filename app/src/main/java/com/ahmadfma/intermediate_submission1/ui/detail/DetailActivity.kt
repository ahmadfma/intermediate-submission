package com.ahmadfma.intermediate_submission1.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ahmadfma.intermediate_submission1.R
import com.ahmadfma.intermediate_submission1.data.model.ListStoryItem
import com.ahmadfma.intermediate_submission1.databinding.ActivityDetailBinding
import com.bumptech.glide.Glide

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private var storyItem: ListStoryItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        storyItem = intent.getParcelableExtra(EXTRA_STORY)
        setUI()
    }

    private fun setUI() = with(binding) {
        storyItem?.let {
            Glide.with(this@DetailActivity)
                .load(it.photoUrl)
                .placeholder(R.color.grey)
                .into(storyImage)
            storyDate.text = it.createdAt
            storyDesc.text = it.description
            storyUsername.text = it.name
        }

        detailToolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    companion object {
        const val EXTRA_STORY = "extra_story"
    }

}