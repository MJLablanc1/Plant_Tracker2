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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(PlantViewModel::class.java)


//        val plantListTemp: ArrayList<PlantItem> = ArrayList()
//        plantListTemp.add(PlantItem("sunflower", "sunflower.png", "47.6101", "-122.2015", "0"))
//        plantListTemp.add(PlantItem("blueberry", "blurberrie.png", "34.0522", "-118.2437", "1"))

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

       /* val enterPlantButton = findViewById<Button>(R.id.settingsBtn)
        enterPlantButton.setOnClickListener {
            val intent = Intent(this, PlantInput::class.java)
            startActivity(intent)
        }*/

        val plantListButton = findViewById<Button>(R.id.PlantListBtn)
        plantListButton.setOnClickListener {
            val intent = Intent(this, PlantListActivity::class.java)
            startActivity(intent)
        }
    }
}

object Validation {

    val newPlantItem = PlantItem("Sun flower", "two", "three", "four", "five","six")

    fun returnPlantObjectName(x: String): String {
        return newPlantItem.plantName
    }

    fun returnPlantObjectDescription(x: String): String {
        return newPlantItem.description
    }
}
