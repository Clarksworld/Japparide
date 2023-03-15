package com.japaride.app.ui.home

import android.R
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Transformations.map
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.japaride.app.databinding.FragmentHomeBinding
import com.japaride.app.network.api.Resource
import com.japaride.app.network.api.auth_api.AuthApi
import com.japaride.app.network.models.user_location_model.UserLocationModel
import com.japaride.app.repositories.auth_repo.AuthenticationRepository
import com.japaride.app.ui.base.BaseFragment
import com.japaride.app.visible
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking


class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding,
        AuthenticationRepository>() {


    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    var latitude = 0.00
    var longitude = 0.00

    var id = ""
    var token = ""

    private val googleMap: GoogleMap? = null

    private val mapView: MapView? = null
    private var mMapFragment: SupportMapFragment? = null

    companion object {

        private const val PERMISSION_REQUEST_ACCESS_LOCATION = 1
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)




        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())

        getCurrentLocation()

        val mapFragment = childFragmentManager.findFragmentById(com.japaride.app.R.id.map) as SupportMapFragment
        mapFragment?.getMapAsync(callback)


        viewModel.locationUpdateResponse.observe(viewLifecycleOwner){

            when(it){

                is Resource.Success -> {
                    binding.updateLocationProgress.visible(false)

                    Toast.makeText(requireContext(), "updated", Toast.LENGTH_SHORT).show()

                }

                is Resource.Error -> {
                    binding.updateLocationProgress.visible(false)
                    Toast.makeText(requireContext(), "$it", Toast.LENGTH_SHORT).show()


                }

                is Resource.Loading -> {

                    binding.updateLocationProgress.visible(true)

                }
            }
        }


    }

    private val callback = OnMapReadyCallback { googleMap ->

        val sydney = LatLng(latitude, longitude)
        googleMap.addMarker(MarkerOptions().position(sydney).title("My Location"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }


    private fun getCurrentLocation() {

        if (checkPermission()) {

            if (isLocationEnabled()) {

                fusedLocationProviderClient.lastLocation.addOnCompleteListener(requireActivity()) { task ->

                    val location: Location? = task.result

                    if (location == null) {

                        Toast.makeText(
                            requireContext(),
                            "Empty Location Received",
                            Toast.LENGTH_SHORT
                        ).show()

                    } else {

                         latitude = location.latitude
                        longitude = location.longitude
//                        Toast.makeText(
//                            requireContext(),
//                            "Location gotten $latitude  $longitude",
//                            Toast.LENGTH_SHORT
//                        ).show()

                        viewModel.locationUpdate(
                            id.toInt(),
                            UserLocationModel(
                                latitude.toString(),
                                longitude.toString()
                            ))

                    }

                }

            } else {

                // settings
                Toast.makeText(requireContext(), "Turn on your location ", Toast.LENGTH_SHORT)
                    .show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }

        } else {

            // request permission
            requestPermission()
        }
    }

    private fun isLocationEnabled(): Boolean {

        val locationManager: LocationManager =
            activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    fun requestPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(), arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ), PERMISSION_REQUEST_ACCESS_LOCATION
        )
    }

    private fun checkPermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            return true
        }

        return false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST_ACCESS_LOCATION) {

            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(requireContext(), "Permission has been granted", Toast.LENGTH_SHORT)
                    .show()
                getCurrentLocation()
            } else {

                Toast.makeText(requireContext(), "Permission has been denied", Toast.LENGTH_SHORT)
                    .show()

            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()

    }

    override fun getViewModel() = HomeViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentHomeBinding.inflate(inflater, container, false)

    override fun getFragmentRepository(): AuthenticationRepository{

        token = runBlocking { userPreference.authToken.first().toString() }
        id = runBlocking { userPreference.userId.first().toString() }


        val api = remoteDataSource.locationUpdate (AuthApi::class.java)

        return AuthenticationRepository(api, userPreference)

    }



}