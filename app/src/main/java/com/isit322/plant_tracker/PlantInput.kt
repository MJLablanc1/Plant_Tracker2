package com.isit322.plant_tracker

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.isit322.plant_tracker.data.RGeoData
import com.isit322.plant_tracker.ui.RGeoDataViewModel
import java.io.File

//Camera functions
//File_name and Request_code are only reference names in this instance
private lateinit var photoFile: File
private const val REQUEST_CODE = 42
private const val FILE_NAME = "photo"



class PlantInput : AppCompatActivity() {

    lateinit var rGeoViewModel: RGeoDataViewModel
    lateinit var rGeoDataObject: RGeoData

    var lat = ""
    var long = ""
    var formattedAddress = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plant_input)

        rGeoViewModel = ViewModelProvider(this).get(RGeoDataViewModel::class.java)

        lat = intent.getStringExtra("lat").toString()
        long = intent.getStringExtra("long").toString()
        getGeoLocation()

        //Sets up the picture button, using an Intent to access the camera to actually take the photo
        val btnTakePicture = findViewById<Button>(R.id.label_PlantPicture)
        btnTakePicture.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            photoFile = getPhotoFile(FILE_NAME)

            // A FileProvider saves the photo, which we will later retrieve for a higher quality preview to the user
            val fileProvider = FileProvider.getUriForFile(this, "com.isit322.plant_tracker.fileprovider", photoFile)
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
            if (takePictureIntent.resolveActivity(this.packageManager) != null ){
                startActivityForResult(takePictureIntent, REQUEST_CODE)
            } else {
                Toast.makeText(this, "Unable to open camera", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //gets the directory for pictures and places a new one for the photo to be taken
    private fun getPhotoFile(fileName: String): File {
        val storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", storageDirectory)
    }

    //Gets the photo that was just taken and displays it as a preview.
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val takenImage = BitmapFactory.decodeFile(photoFile.absolutePath)
            val imageView = findViewById<ImageView>(R.id.PlantImage)
            imageView.setImageBitmap(takenImage)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    fun getGeoLocation() {
        val latLong = lat + "," + long;
        rGeoViewModel.getRGeoData(latLong, this)
        rGeoViewModel.RGeoDataResponse.observe(this) {
            rGeoDataObject = it
            //Get the formatted address of choice by getting the 3rd result list from the geo object
            formattedAddress = rGeoDataObject.results[1].formatted_address
            Log.i("geo", rGeoDataObject.results[1].formatted_address)
            val locationText = findViewById<TextView>(R.id.Location)
            locationText.setText(formattedAddress)
        }
    }

    //Get Plant text inputs

    //Upload Plant data to database
    //Refresh map database

}


