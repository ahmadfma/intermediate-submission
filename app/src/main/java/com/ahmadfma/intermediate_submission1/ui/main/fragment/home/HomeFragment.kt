package com.ahmadfma.intermediate_submission1.ui.main.fragment.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.ahmadfma.intermediate_submission1.R
import com.ahmadfma.intermediate_submission1.data.Result
import com.ahmadfma.intermediate_submission1.databinding.FragmentHomeBinding
import com.ahmadfma.intermediate_submission1.viewmodel.StoryViewModel
import com.ahmadfma.intermediate_submission1.viewmodel.ViewModelFactory


class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding
    private lateinit var viewModel: StoryViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initVariable()
        initListener()
    }

    private fun initVariable() {
        viewModel = ViewModelProvider(requireActivity(), ViewModelFactory.getInstance())[StoryViewModel::class.java]
    }

    private fun initListener() {
        viewModel.getStories().observe(viewLifecycleOwner) { result ->
            when(result) {
                is Result.Error -> {
                    Log.e(TAG, "getStories: error : ${result.error}")
                    showLoading(false)
                }
                is Result.Loading -> {
                    showLoading(true)
                }
                is Result.Success -> {
                    showLoading(false)
                    val response = result.data
                    if(response != null) {
                        if(!response.error) {
                            Log.d(TAG, "getStories: success : ${result.data.listStory}")
                        } else {
                            Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(requireContext(), getString(R.string.error), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) = with(binding) {
        when(isLoading) {
            true -> {
                homeProgressBar.visibility = View.VISIBLE
                rvStories.visibility = View.GONE
            }
            false -> {
                homeProgressBar.visibility = View.GONE
                rvStories.visibility = View.VISIBLE
            }
        }
    }

    companion object {
        private const val TAG = "HomeFragment"
    }

}