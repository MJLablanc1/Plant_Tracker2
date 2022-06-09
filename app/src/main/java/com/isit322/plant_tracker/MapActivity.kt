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
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.isit322.artworklist.data.PlantItem

class MapActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    var long = 0.0
    var lat = 0.0

    var plantData: ArrayList<PlantItem> = ArrayList()

    private var mapHere: GoogleMap? = null
    private var mMapView: MapView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val actionBar = getSupportActionBar()
        if (actionBar!= null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
        }

        val enterPlantButton = findViewById<Button>(R.id.MaptoPlantbtn)
        enterPlantButton.setOnClickListener {
            val intent = Intent(this, PlantInput::class.java)
            intent.putExtra("lat", lat.toString())
            intent.putExtra("long", long.toString())
            startActivity(intent)
        }

        plantData = intent.getParcelableArrayListExtra("plantData")!!
        Log.i("size", plantData.size.toString())

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

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
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
                        Log.i("last", "Got current location")
                        long = location.longitude
                        lat = location.latitude
                        val newOne = LatLng(lat, long)
                        val newZoom = 6F
                        mapHere?.animateCamera(CameraUpdateFactory.newLatLngZoom(newOne, newZoom))
                        mapHere?.isMyLocationEnabled = true
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
        this.mapHere = map

        map.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.mapstyle))

        for(plant in plantData) {
            if (!plant.latitude.isNullOrEmpty() && !plant.longitude.isNullOrEmpty()) {
                val lat = plant.latitude.toDouble()
                val long = plant.longitude.toDouble()
                var newMarker: Marker? = null
                newMarker = map.addMarker(MarkerOptions()
                    .position(LatLng(lat, long))
                    .title(plant.plantName)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.circle2)))
                newMarker?.tag = plant.id
            }
        }
        map.setOnMarkerClickListener(this)
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        val markerTag = marker.tag
        val index = plantData.indexOfFirst {
            it.id == markerTag
        }

        if (index != -1) {
            val plantDataHere = plantData[index]
            val intent = Intent(this, PlantView::class.java)
            intent.putExtra("markerPlantData", plantDataHere)
            startActivity(intent)
            return false
        }
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

