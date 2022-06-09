package com.isit322.plant_tracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.isit322.artworklist.data.PlantItem
import com.isit322.plant_tracker.data.RGeoData
import com.isit322.plant_tracker.ui.RGeoDataViewModel

class PlantView : AppCompatActivity() {

    lateinit var rGeoViewModel: RGeoDataViewModel
    lateinit var rGeoDataObject: RGeoData

    var formattedAddress = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plant_view)

        val actionBar = getSupportActionBar()
        if (actionBar!= null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
        }

        //Get the parcelable extra plant object coming from the map activity and storing it in plantData
        val plantData: PlantItem = intent.getParcelableExtra("markerPlantData")!!

        rGeoViewModel = ViewModelProvider(this).get(RGeoDataViewModel::class.java)

        val latLong = plantData.latitude + "," + plantData.longitude;
        rGeoViewModel.getRGeoData(latLong, this)
        rGeoViewModel.RGeoDataResponse.observe(this) {
            if (!plantData.latitude.isNullOrEmpty() && !plantData.longitude.isNullOrEmpty() && it != null) {
                rGeoDataObject = it
                if (rGeoDataObject.results.count() > 0) {
                    //Get the formatted address of choice by getting the 3rd result list from the geo object
                    formattedAddress = rGeoDataObject.results[0].formatted_address
                    Log.i("geo", rGeoDataObject.results[0].formatted_address)
                    val locationText = findViewById<TextView>(R.id.ViewPlantDescription)
                    locationText.setText(formattedAddress)
                }
            }
        }

        //Setting the plant name text view from the plant object
        val plantName = findViewById<TextView>(R.id.ViewPlantName)
        plantName.setText(plantData.plantName)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}