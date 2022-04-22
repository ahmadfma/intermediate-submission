package com.ahmadfma.intermediate_submission1.ui.map

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.ahmadfma.intermediate_submission1.R
import com.ahmadfma.intermediate_submission1.data.Result
import com.ahmadfma.intermediate_submission1.data.model.GetStoryResponse
import com.ahmadfma.intermediate_submission1.data.model.ListStoryItem
import com.ahmadfma.intermediate_submission1.databinding.ActivityMapsBinding
import com.ahmadfma.intermediate_submission1.viewmodel.StoryViewModel
import com.ahmadfma.intermediate_submission1.viewmodel.ViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var viewModel: StoryViewModel
    private lateinit var boundsBuilder: LatLngBounds.Builder
    private var listStory : List<ListStoryItem>? = null
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

    private fun initListener() {
        viewModel.getStoriesWithLocation().observe(this) { result ->
            when(result) {
                is Result.Loading -> {
                    Log.d(TAG, "getStoriesWithLocation: loading")
                }
                is Result.Error -> {
                    Log.e(TAG, "getStoriesWithLocation: error = ${result.error}")
                }
                is Result.Success -> {
                    Log.d(TAG, "getStoriesWithLocation: success : ${result.data}")
                    val response = result.data
                    if(response != null) {
                        if(!response.error) {
                            setMarkers(response)
                        } else {
                            Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show()
                    }
                }
            }
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
            moveCamera(0)
        } else {
            Toast.makeText(this, getString(R.string.empty), Toast.LENGTH_SHORT).show()
        }
    }

    private fun moveCamera(index: Int) {
        val lat = listStory?.get(index)?.latitude
        val lon = listStory?.get(index)?.longitude
        if(lat != null && lon != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(lat, lon), 10f))
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true
    }
}