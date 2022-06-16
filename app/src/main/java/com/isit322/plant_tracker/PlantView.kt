package com.isit322.plant_tracker

import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.isit322.artworklist.data.PlantItem
import com.isit322.plant_tracker.data.RGeoData
import com.isit322.plant_tracker.ui.RGeoDataViewModel
import com.squareup.picasso.Picasso
import java.net.URI

class PlantView : AppCompatActivity() {

    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    lateinit var rGeoViewModel: RGeoDataViewModel
    lateinit var rGeoDataObject: RGeoData

    var formattedAddress = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plant_view)

        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference

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
                    val locationText = findViewById<TextView>(R.id.ViewLocation)
                    locationText.setText(formattedAddress)
                }
            }

        }


        //Setting the plant view location text to the plant object's
        val plantDesc = findViewById<TextView>(R.id.ViewPlantDescription)
        plantDesc.setText(plantData.description)

        //Setting the plant name text view from the plant object
        val plantName = findViewById<TextView>(R.id.ViewPlantName)
        plantName.setText(plantData.plantName)

        //Setting Plant image in image view
        val imageView = findViewById<ImageView>(R.id.ViewPlantImage)

        val id = plantData.id
        val relPath = "image_$id.png"

        var imageItem: StorageReference? = null

        val storage = Firebase.storage
        storage.reference.listAll()
            .addOnSuccessListener {
                it.items.forEach {
                    item ->
                    if (relPath.equals(item.name)) {
                        imageItem = item
                        Log.d("IMAGE", "Result: " + item.toString() + "|" + item.name)
                    }
                }

                var uri: Uri?
                imageItem?.downloadUrl?.addOnCompleteListener(object: OnCompleteListener<Uri> {

                    override fun onComplete(p0: Task<Uri>) {
                        uri = p0.getResult()
                        Log.d("URI", "uri: " + uri)
                        Picasso.get().load(uri).into(imageView)
                    }
                })

            }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}