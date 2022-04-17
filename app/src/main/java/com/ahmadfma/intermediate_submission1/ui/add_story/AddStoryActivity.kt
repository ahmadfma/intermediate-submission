package com.ahmadfma.intermediate_submission1.ui.add_story

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.ahmadfma.intermediate_submission1.R
import com.ahmadfma.intermediate_submission1.databinding.ActivityAddStoryBinding
import com.ahmadfma.intermediate_submission1.viewmodel.StoryViewModel
import com.ahmadfma.intermediate_submission1.viewmodel.ViewModelFactory

class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var viewModel: StoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initVariable()
        initListener()
    }

    private fun initVariable() {
        viewModel = ViewModelProvider(this, ViewModelFactory.getInstance())[StoryViewModel::class.java]
    }

    private fun initListener() {

    }
}