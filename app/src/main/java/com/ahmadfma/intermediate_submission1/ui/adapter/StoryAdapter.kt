package com.ahmadfma.intermediate_submission1.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ahmadfma.intermediate_submission1.R
import com.ahmadfma.intermediate_submission1.data.model.ListStoryItem
import com.ahmadfma.intermediate_submission1.databinding.ItemStoryBinding
import com.ahmadfma.intermediate_submission1.helper.convertToDate
import com.bumptech.glide.Glide

class StoryAdapter: PagingDataAdapter<ListStoryItem, StoryAdapter.Holder>(DIFF_CALLBACK) {

    private lateinit var context: Context
    private lateinit var onItemClickListener: OnItemClickListener

    inner class Holder(private val binding: ItemStoryBinding, private val onItemClickListener: OnItemClickListener): RecyclerView.ViewHolder(binding.root) {
        fun bind(storyItem: ListStoryItem) = with(binding){
            Glide.with(context)
                .load(storyItem.photoUrl)
                .placeholder(R.color.grey)
                .into(storyImage)
            storyDate.text = context.getString(R.string.upload, storyItem.createdAt?.convertToDate())
            storyDesc.text = storyItem.description
            storyUsername.text = storyItem.name

            root.setOnClickListener {
                onItemClickListener.onItemClick(storyItem, this)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        context = parent.context
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding, onItemClickListener)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    fun setListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    interface OnItemClickListener {
        fun onItemClick(listStoryItem: ListStoryItem, storyBinding: ItemStoryBinding)
    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<ListStoryItem> = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldStory: ListStoryItem, newStory: ListStoryItem): Boolean {
                return oldStory.photoUrl == newStory.photoUrl
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldStory: ListStoryItem, newStory: ListStoryItem): Boolean {
                return oldStory == newStory
            }
        }
    }

}