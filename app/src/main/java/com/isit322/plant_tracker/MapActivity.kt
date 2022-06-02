package com.isit322.plant_tracker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.isit322.artworklist.data.PlantItem
import com.isit322.plant_tracker.data.RGeoData
import com.isit322.plant_tracker.ui.RGeoDataViewModel

class MapActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var rGeoViewModel: RGeoDataViewModel
    lateinit var rGeoDataObject: RGeoData

    var long = 0.0
    var lat = 0.0

    var plantData: ArrayList<PlantItem> = ArrayList()

    private var mMapView: MapView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val enterPlantButton = findViewById<Button>(R.id.MaptoPlantbtn)
        enterPlantButton.setOnClickListener {
            val intent = Intent(this, PlantInput::class.java)
            intent.putExtra("lat", lat.toString())
            intent.putExtra("long", long.toString())
            startActivity(intent)
        }

        plantData = intent.getParcelableArrayListExtra("plantData")!!
        Log.i("size", plantData.size.toString())

        //Initializing RGeoDataViewModel to be used to make api call for reverse Geocoding data
        rGeoViewModel = ViewModelProvider(this).get(RGeoDataViewModel::class.java)

        // *** IMPORTANT ***
        // MapView requires that the Bundle you pass contain _ONLY_ MapView SDK
        // objects or sub-Bundles.
        var mapViewBundle: Bundle? = null
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY)
        }
        mMapView = findViewById<View>(R.id.mapView) as MapView
        mMapView!!.onCreate(mapViewBundle)
        mMapView!!.getMapAsync(this)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        getCurrentLocation()
    }

    public override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        var mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY)
        if (mapViewBundle == null) {
            mapViewBundle = Bundle()
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle)
        }
        mMapView!!.onSaveInstanceState(mapViewBundle)
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                fusedLocationProviderClient.lastLocation.addOnCompleteListener(this) { task ->
                    val location: Location?=task.result
                    if (location == null) {
                        Toast.makeText(this,"Null Recieved", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this,"Get Success", Toast.LENGTH_SHORT).show()
                        long = location.longitude
                        lat = location.latitude

                        // USING Longitude and latitude values to convert to fields such as city, state etc. using Reverse Geo Coding
                        var latLong = "" + lat + "," + long + "";
                        rGeoViewModel.getRGeoData(latLong, this)
                        rGeoViewModel.RGeoDataResponse.observe(this) {
                            rGeoDataObject = it
                        }
                    }
                }
            } else {
                Toast.makeText(this,"Turn on location", Toast.LENGTH_SHORT).show()
                //val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                //startActivity(intent)
            }
        } else {
            requestPermission()
        }
    }

    override fun onResume() {
        super.onResume()
        mMapView!!.onResume()
    }

    override fun onStart() {
        super.onStart()
        mMapView!!.onStart()
    }

    override fun onStop() {
        super.onStop()
        mMapView!!.onStop()
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(map: GoogleMap) {
        map.isMyLocationEnabled = true
        for(plant in plantData) {
            val lat = plant.latitude.toDouble()
            val long = plant.longitude.toDouble()
            var newMarker: Marker? = null
            newMarker = map.addMarker(MarkerOptions().position(LatLng(lat, long)).title(plant.plantName))
            newMarker?.tag = plant.id
        }

        map.setOnMarkerClickListener(this)
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        //Toast.makeText(this, "${marker.tag}", Toast.LENGTH_SHORT).show()
        val markerTag = marker.tag
        val index = plantData.indexOfFirst {
            it.id == markerTag
        }
        val plantDataHere = plantData[index]
        //Log.i("tempData", plantDataHere.plantName)
        val intent = Intent(this, PlantView::class.java)
        intent.putExtra("markerPlantData", plantDataHere)
        startActivity(intent)
        return false
    }

    override fun onPause() {
        mMapView!!.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        mMapView!!.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mMapView!!.onLowMemory()
    }

    companion object {
        private const val PERMISSION_REQUEST_ACCESS_LOCATION=100
        private const val MAPVIEW_BUNDLE_KEY = "MapViewBundleKey"
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager=getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(this,
            android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            return true
        }

        return false
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,
        android.Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_REQUEST_ACCESS_LOCATION)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST_ACCESS_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(applicationContext, "Granted", Toast.LENGTH_SHORT).show()
                getCurrentLocation()
            } else {
                Toast.makeText(applicationContext, "Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

