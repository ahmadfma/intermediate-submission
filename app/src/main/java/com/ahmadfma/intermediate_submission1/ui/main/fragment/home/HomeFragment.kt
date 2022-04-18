package com.ahmadfma.intermediate_submission1.ui.main.fragment.home

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahmadfma.intermediate_submission1.R
import com.ahmadfma.intermediate_submission1.data.Result
import com.ahmadfma.intermediate_submission1.data.model.GetStoryResponse
import com.ahmadfma.intermediate_submission1.data.model.ListStoryItem
import com.ahmadfma.intermediate_submission1.databinding.FragmentHomeBinding
import com.ahmadfma.intermediate_submission1.ui.adapter.StoryAdapter
import com.ahmadfma.intermediate_submission1.ui.detail.DetailActivity
import com.ahmadfma.intermediate_submission1.viewmodel.StoryViewModel
import com.ahmadfma.intermediate_submission1.viewmodel.ViewModelFactory
import androidx.core.util.Pair
import com.ahmadfma.intermediate_submission1.databinding.ItemStoryBinding
import com.ahmadfma.intermediate_submission1.widgets.ImageBannerWidget

class HomeFragment : Fragment() {
    private lateinit var binding : FragmentHomeBinding
    private lateinit var viewModel: StoryViewModel
    private lateinit var storyAdapter: StoryAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(inflater)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initVariable()
        initListener()
    }

    private fun initVariable() {
        viewModel = ViewModelProvider(requireActivity(), ViewModelFactory.getInstance())[StoryViewModel::class.java]
        storyAdapter = StoryAdapter()
    }

    private fun initListener() {
        storyAdapter.setListener(object : StoryAdapter.OnItemClickListener {
            override fun onItemClick(listStoryItem: ListStoryItem, storyBinding: ItemStoryBinding) {
                val intent = Intent(requireActivity(), DetailActivity::class.java)
                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        requireActivity(),
                        Pair(storyBinding.storyImage, getString(R.string.transition_image)),
                        Pair(storyBinding.storyUsername, getString(R.string.transition_username)),
                        Pair(storyBinding.storyDesc, getString(R.string.transition_description)),
                        Pair(storyBinding.storyDate, getString(R.string.transition_date)),
                    )
                intent.putExtra(DetailActivity.EXTRA_STORY, listStoryItem)
                startActivity(intent, optionsCompat.toBundle())
            }
        })

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
                            setUi(response)
                        } else {
                            Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(requireContext(), getString(R.string.error), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.homeToolbar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.settingMenu -> {
                    startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                    return@setOnMenuItemClickListener  true
                }
            }
            return@setOnMenuItemClickListener false
        }
    }

    private fun setUi(response: GetStoryResponse) = with(binding) {
        if(response.listStory.isNotEmpty()) {
            Log.d(TAG, "setUi: $response")
            storyAdapter.submitList(response.listStory)
            rvStories.layoutManager = LinearLayoutManager(requireContext())
            rvStories.setHasFixedSize(true)
            rvStories.adapter = storyAdapter
            updateStackWidget(response)
        } else {
            Toast.makeText(requireContext(), getString(R.string.empty), Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateStackWidget(response: GetStoryResponse) {
        val intent = Intent(requireContext(), ImageBannerWidget::class.java)
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        val ids = AppWidgetManager.getInstance(requireContext().applicationContext).getAppWidgetIds(ComponentName(requireContext(), ImageBannerWidget::class.java))
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
        intent.putExtra(ImageBannerWidget.EXTRA_ITEM, response)
        requireActivity().sendBroadcast(intent)
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