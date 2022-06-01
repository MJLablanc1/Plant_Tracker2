package com.isit322.plant_tracker

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.isit322.artworklist.adapters.AdapterRecycler
import com.isit322.artworklist.data.PlantItem
import com.isit322.artworklist.ui.PlantViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_plant_list.*

class MainActivity : AppCompatActivity() {
    lateinit var viewModel: PlantViewModel
    var plantList: List<PlantItem>? = ArrayList()
    var plantListTemp: ArrayList<PlantItem> = ArrayList()

    @RequiresApi(Build.VERSION_CODES.N)
    val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(android.Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                //precise location access granted.
            }
            permissions.getOrDefault(android.Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                //only approximate location access granted.
            }
            else -> {
                //No location access granted
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(PlantViewModel::class.java)


//        var plantListTemp: ArrayList<PlantItem> = ArrayList()
//        plantListTemp.add(PlantItem("sunflower", "sunflower.png", "23232", "hi"))
//        plantListTemp.add(PlantItem("blueberry", "blurberrie.png", "11111", "give"))

        /*
            locationPermissionRequest.launch(arrayOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION))
        */
            viewModel.getPlant(this)
            viewModel.plantResponse.observe(this) {
                if (!it.isEmpty()) {
                    plantList = it
                    Log.i("data", plantList!!.size.toString())
                    plantListTemp = plantList as ArrayList<PlantItem>
                    Log.i("data2", plantListTemp.size.toString())
                }
            }


        val startButton = findViewById<Button>(R.id.StartBtn)
        startButton.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            Log.i("data3", plantListTemp.size.toString())
            intent.putParcelableArrayListExtra("plantData", plantListTemp)
            startActivity(intent)
        }

        val enterPlantButton = findViewById<Button>(R.id.settingsBtn)
        enterPlantButton.setOnClickListener {
            val intent = Intent(this, PlantInput::class.java)
            startActivity(intent)
        }

        val plantListButton = findViewById<Button>(R.id.PlantListBtn)
        plantListButton.setOnClickListener {
            val intent = Intent(this, PlantListActivity::class.java)
            startActivity(intent)
        }
    }
}
