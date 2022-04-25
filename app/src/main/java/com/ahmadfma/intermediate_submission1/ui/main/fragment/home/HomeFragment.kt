package com.ahmadfma.intermediate_submission1.ui.main.fragment.home

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahmadfma.intermediate_submission1.R
import com.ahmadfma.intermediate_submission1.data.model.GetStoryResponse
import com.ahmadfma.intermediate_submission1.data.model.ListStoryItem
import com.ahmadfma.intermediate_submission1.databinding.FragmentHomeBinding
import com.ahmadfma.intermediate_submission1.ui.adapter.StoryAdapter
import com.ahmadfma.intermediate_submission1.ui.detail.DetailActivity
import com.ahmadfma.intermediate_submission1.viewmodel.StoryViewModel
import com.ahmadfma.intermediate_submission1.viewmodel.ViewModelFactory
import androidx.core.util.Pair
import com.ahmadfma.intermediate_submission1.databinding.ItemStoryBinding
import com.ahmadfma.intermediate_submission1.ui.adapter.LoadingStateAdapter
import com.ahmadfma.intermediate_submission1.ui.maps.MapsActivity
import com.ahmadfma.intermediate_submission1.widgets.ImageBannerWidget

class HomeFragment : Fragment() {
    private lateinit var binding : FragmentHomeBinding
    private lateinit var viewModel: StoryViewModel
    private lateinit var storyAdapter: StoryAdapter
    private companion object {
        const val TAG = "HomeFragment"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(inflater)
        setHasOptionsMenu(true)
        initVariable()
        initListener()
        return binding.root
    }

    private fun initVariable() {
        viewModel = ViewModelProvider(requireActivity(), ViewModelFactory.getInstance(requireContext()))[StoryViewModel::class.java]
        storyAdapter = StoryAdapter()
        with(binding) {
            rvStories.layoutManager = LinearLayoutManager(requireContext())
            rvStories.setHasFixedSize(true)
            rvStories.adapter = storyAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    storyAdapter.retry()
                }
            )
        }
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

        viewModel.stories.observe(viewLifecycleOwner) {
            Log.d(TAG, "stories: ${storyAdapter.snapshot().items.size}")
            updateStackWidget(storyAdapter.snapshot().items)
            storyAdapter.submitData(lifecycle, it)
        }

        binding.homeToolbar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.settingMenu -> {
                    startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                    return@setOnMenuItemClickListener  true
                }
                R.id.gotoMaps -> {
                    startActivity(Intent(requireActivity(), MapsActivity::class.java))
                }
            }
            return@setOnMenuItemClickListener false
        }

    }

    private fun updateStackWidget(stories: List<ListStoryItem>) {
        if(stories.isNotEmpty()) {
            Log.d(TAG, "updateStackWidget: send broadcast")
            val response = GetStoryResponse(
                listStory = stories,
                message = "updateStackWidget",
                error = false
            )
            val intent = Intent(requireContext(), ImageBannerWidget::class.java)
            intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            val ids = AppWidgetManager.getInstance(requireContext().applicationContext).getAppWidgetIds(ComponentName(requireContext(), ImageBannerWidget::class.java))
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
            intent.putExtra(ImageBannerWidget.EXTRA_ITEM, response)
            requireActivity().sendBroadcast(intent)   
        }
    }

    override fun onStart() {
        super.onStart()
        storyAdapter.refresh()
    }


}