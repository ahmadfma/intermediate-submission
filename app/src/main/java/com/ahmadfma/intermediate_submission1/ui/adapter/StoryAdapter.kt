package com.ahmadfma.intermediate_submission1.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.ahmadfma.intermediate_submission1.R
import com.ahmadfma.intermediate_submission1.data.model.ListStoryItem
import com.ahmadfma.intermediate_submission1.databinding.ItemStoryBinding
import com.bumptech.glide.Glide

class StoryAdapter(private val stories : List<ListStoryItem>): RecyclerView.Adapter<StoryAdapter.Holder>() {

    private lateinit var context: Context

    inner class Holder(private val binding: ItemStoryBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(storyItem: ListStoryItem) = with(binding){
            Glide.with(context)
                .load(storyItem.photoUrl)
                .placeholder(R.color.grey)
                .into(storyImage)
            storyDate.text = storyItem.createdAt
            storyDesc.text = storyItem.description
            storyUsername.text = storyItem.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        context = parent.context
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(stories[position])
    }

    override fun getItemCount(): Int = stories.size
}