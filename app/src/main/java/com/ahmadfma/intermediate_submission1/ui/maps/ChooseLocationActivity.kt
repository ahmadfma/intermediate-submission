package com.ahmadfma.intermediate_submission1.ui.maps

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
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
    private val defaultLocation = LatLng( -5.1341527366777004, 119.48807445857393)
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
        initVariable()
        initListener()
    }

    private fun initVariable() = with(binding) {
        val latitude = intent.getFloatExtra(EXTRA_LATITUDE, 0f)
        val longitude = intent.getFloatExtra(EXTRA_LONGITUDE, 0f)
        if(latitude != 0f && longitude != 0f) {
            selectedLatLng = LatLng(latitude.toDouble(), longitude.toDouble())
            mapPick.mapLatLng.text = StringBuilder("$latitude, ").append("$longitude")
        } else {
            mapPick.mapLatLng.text = "-"
        }
    }

    private fun initListener() = with(binding) {
        mapPick.selectBtn.setOnClickListener {
            if(selectedLatLng != null) {
                val intent = Intent()
                intent.putExtra(EXTRA_LATITUDE, selectedLatLng?.latitude?.toFloat())
                intent.putExtra(EXTRA_LONGITUDE, selectedLatLng?.longitude?.toFloat())
                setResult(RESULT_OK, intent)
                finish()
            } else {
                Toast.makeText(this@ChooseLocationActivity, "Please select your location", Toast.LENGTH_SHORT).show()
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
        if(selectedLatLng != null) {
            selectedLatLng?.let {
                mMap.addMarker(MarkerOptions().position(it).title("Your location"))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(it, 20f))
            }
        } else {
            mMap.addMarker(MarkerOptions().position(defaultLocation).title("Your location"))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 20f))
        }
        setMyLocation()
    }

    private fun setMyLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        mMap.isMyLocationEnabled = true
    }


}