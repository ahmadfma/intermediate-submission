package com.ahmadfma.intermediate_submission1.ui.maps

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.ahmadfma.intermediate_submission1.R
import com.ahmadfma.intermediate_submission1.databinding.ActivityChooseLocationBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlin.text.StringBuilder

class ChooseLocationActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityChooseLocationBinding
    private var selectedLatLng: LatLng? = null
    companion object {
        const val EXTRA_LATITUDE = "latitude"
        const val EXTRA_LONGITUDE = "longitude"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.mapPick.selectBtn.setOnClickListener {
            if(selectedLatLng != null) {
                val intent = Intent()
                intent.putExtra(EXTRA_LATITUDE, selectedLatLng?.latitude?.toFloat())
                intent.putExtra(EXTRA_LONGITUDE, selectedLatLng?.longitude?.toFloat())
                setResult(RESULT_OK, intent)
                finish()
            } else {
                Toast.makeText(this, "Please select your location", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnMapClickListener {
            selectedLatLng = it
            mMap.clear()
            mMap.addMarker(MarkerOptions().position(it).title("Your location"))
            binding.mapPick.mapLatLng.text = StringBuilder("${it.latitude.toFloat()}").append(", ${it.longitude.toFloat()}")
        }
        val sydney = LatLng( -5.133419,119.503139)
        mMap.addMarker(MarkerOptions().position(sydney).title("Your location"))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 20f))
    }
}