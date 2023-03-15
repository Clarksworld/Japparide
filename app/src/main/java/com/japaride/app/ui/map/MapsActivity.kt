package com.japaride.app.ui.map

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.japaride.app.R
import com.japaride.app.databinding.ActivityMapsBinding
import com.japaride.app.ui.webview.WebViewActivity

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private  lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    companion object{
        private const val LOCATION_REQUEST_CODE = 10
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)

        supportActionBar?.hide();


        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this )


    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.setOnMarkerClickListener(this)

//        // Add a marker in Sydney and move the camera
//        val sydney = LatLng(-34.0, 151.0)
//        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        setUpMap()
        destinationBottomSheet()
    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_REQUEST_CODE)

            return
        }
        mMap.isMyLocationEnabled = true
        fusedLocationClient.lastLocation.addOnSuccessListener(this){ location ->

            if (location != null){

                lastLocation = location

                val currentLocation = LatLng(location.latitude, location.longitude )
                placeMarkerOnMap(currentLocation)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 12f))
            }
        }
    }

    private fun placeMarkerOnMap(currentLocation: LatLng) {
        val markerOptions = MarkerOptions().position(currentLocation )
        markerOptions.title("$currentLocation")
        mMap.addMarker(markerOptions)
    }

    override fun onMarkerClick(p0: Marker): Boolean = false

    fun destinationBottomSheet(){

        BottomSheetBehavior.from(binding.bottomSheetFrame).apply {

            peekHeight = 200
            this.state = BottomSheetBehavior.STATE_EXPANDED

            binding.layoutWrapper.setOnClickListener {

                val intent = Intent(baseContext, WebViewActivity::class.java)
                startActivity(intent)
                finish()

            }
        }


    }


}
