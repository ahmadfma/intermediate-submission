package com.ahmadfma.intermediate_submission1.ui.maps

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.lifecycle.ViewModelProvider
import com.ahmadfma.intermediate_submission1.R
import com.ahmadfma.intermediate_submission1.data.Result
import com.ahmadfma.intermediate_submission1.data.model.GetStoryResponse
import com.ahmadfma.intermediate_submission1.data.model.ListStoryItem
import com.ahmadfma.intermediate_submission1.databinding.ActivityMapsBinding
import com.ahmadfma.intermediate_submission1.helper.convertToDate
import com.ahmadfma.intermediate_submission1.ui.detail.DetailActivity
import com.ahmadfma.intermediate_submission1.viewmodel.StoryViewModel
import com.ahmadfma.intermediate_submission1.viewmodel.ViewModelFactory
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var viewModel: StoryViewModel
    private var listStory : List<ListStoryItem>? = null
    private var currentShowIndex = 0
    private companion object {
        const val TAG = "MapsActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initVariable()
        initListener()
    }

    private fun initVariable() {
        viewModel = ViewModelProvider(this, ViewModelFactory.getInstance(this))[StoryViewModel::class.java]
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun initListener() = with(binding) {
        viewModel.getStoriesWithLocation().observe(this@MapsActivity) { result ->
            when(result) {
                is Result.Loading -> {
                    showLoadingInBottomStory(true)
                }
                is Result.Error -> {
                    showLoadingInBottomStory(false)
                    Toast.makeText(this@MapsActivity, result.error, Toast.LENGTH_SHORT).show()
                }
                is Result.Success -> {
                    val response = result.data
                    if(response != null) {
                        if(!response.error) {
                            showLoadingInBottomStory(false)
                            setMarkers(response)
                        } else {
                            Toast.makeText(this@MapsActivity, response.message, Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@MapsActivity, getString(R.string.error), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        prevBtn.setOnClickListener {
           listStory?.let {
               if(currentShowIndex != 0) {
                   currentShowIndex--
               } else {
                   currentShowIndex = it.size-1
               }
               moveCamera(currentShowIndex)
           }
        }

        nextBtn.setOnClickListener {
            listStory?.let {
                if(currentShowIndex != it.size-1) {
                    currentShowIndex++
                } else {
                    currentShowIndex = 0
                }
                moveCamera(currentShowIndex)
            }
        }

        mapsToolbar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.menu1 -> {
                    Toast.makeText(this@MapsActivity, "menu1", Toast.LENGTH_SHORT).show()
                    return@setOnMenuItemClickListener true
                }
            }
            return@setOnMenuItemClickListener false
        }

        mapsToolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun setMarkers(response: GetStoryResponse) {
        listStory = response.listStory
        if(listStory != null) {
            listStory?.forEach {
                val lat = it.latitude
                val lon = it.longitude
                if(lat != null && lon != null) {
                    val position = LatLng(lat, lon)
                    mMap.addMarker(MarkerOptions().position(position).title(it.name))
                }
            }
            moveCamera(currentShowIndex)
        } else {
            Toast.makeText(this, getString(R.string.empty), Toast.LENGTH_SHORT).show()
        }
    }

    private fun moveCamera(index: Int) = with(binding) {
        val data = listStory?.get(index)
        if(data != null) {
            if(data.latitude != null && data.longitude != null) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(data.latitude, data.longitude), 10f))
                Glide.with(this@MapsActivity)
                    .load(data.photoUrl)
                    .placeholder(R.drawable.talk)
                    .error(R.drawable.talk2)
                    .into(mapStory.mapStoryImage)
                mapStory.mapStoryName.text = data.name
                mapStory.mapStoryDesc.text = data.description?.trim()
                mapStory.mapStoryDate.text = data.createdAt?.convertToDate()
                mapStory.mapStoryImage.setOnClickListener {
                    val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        this@MapsActivity,
                        Pair(mapStory.mapStoryImage, getString(R.string.transition_image)),
                        Pair(mapStory.mapStoryName, getString(R.string.transition_username)),
                        Pair(mapStory.mapStoryDesc, getString(R.string.transition_description)),
                        Pair(mapStory.mapStoryDate, getString(R.string.transition_date)),
                    )
                    val intent = Intent(this@MapsActivity, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.EXTRA_STORY, data)
                    startActivity(intent, optionsCompat.toBundle())
                }
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        setMapStyle()
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true
    }

    private fun setMapStyle() {
        try {
            val success = mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (exception: Exception) {
            Log.e(TAG, "Can't find style. Error: ", exception)
        }
    }

    private fun showLoadingInBottomStory(isLoading: Boolean) = with(binding) {
        if(isLoading) {
            mapStory.group.visibility = View.GONE
            mapStory.mapProgressBar.visibility = View.VISIBLE
        } else {
            mapStory.group.visibility = View.VISIBLE
            mapStory.mapProgressBar.visibility = View.GONE
        }
    }

}