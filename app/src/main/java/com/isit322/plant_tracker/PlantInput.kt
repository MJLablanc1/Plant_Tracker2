package com.isit322.plant_tracker

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.FileProvider
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File

//Camera functions
//File_name and Request_code are only reference names in this instance




class PlantInput : AppCompatActivity() {
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    private var filePath: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        val plantDatabase = openOrCreateDatabase("PlantDatabaseTest", MODE_PRIVATE, null)

        plantDatabase.execSQL("CREATE TABLE IF NOT EXISTS PlantTable(PlantID integer primary key autoincrement, PlantName VARCHAR, Location VARCHAR, Description VARCHAR, RelativePath VARCHAR);")

        lateinit var photoFile: File
        val REQUEST_CODE = 42
        val FILE_NAME = "photo"

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plant_input)

        //Sets up the picture button, using an Intent to access the camera to actually take the photo
        val btnTakePicture = findViewById<Button>(R.id.label_PlantPicture)
        val btnSubmit = findViewById<Button>(R.id.AddPlantBtn)

        btnSubmit.setOnClickListener {
            val plantName = findViewById<TextView>(R.id.PlantName).text
            val plantLocation = findViewById<TextView>(R.id.Location).text
            val plantDescription = findViewById<TextView>(R.id.PlantDescription).text

            plantDatabase.execSQL("INSERT INTO PlantTable VALUES (NULL, '$plantName', '$plantLocation', '$plantDescription', NULL);")

            val newIDRaw: Cursor = plantDatabase.rawQuery("SELECT MAX(PlantID) FROM PlantTable", null)
            newIDRaw.moveToFirst()
            val finalID = newIDRaw.getString(0).toInt()
            val relPath = "image_$finalID.png"
            plantDatabase.execSQL("UPDATE PlantTable SET RelativePath = \"" + relPath +
                    "\" WHERE PlantID = " + finalID)
            Toast.makeText(this, "Plant added to database", Toast.LENGTH_SHORT).show()

            val newPlantName: Cursor = plantDatabase.rawQuery("SELECT PlantName FROM PlantTable WHERE PlantID = $finalID", null)
            newPlantName.moveToFirst()
            val plantNameDisplay = newPlantName.getString(0)


            findViewById<Button>(R.id.AddPlantBtn).text = "$plantNameDisplay"
            
        }


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
        lateinit var photoFile: File
        val REQUEST_CODE = 42

        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val takenImage = BitmapFactory.decodeFile(photoFile.absolutePath)
            val imageView = findViewById<ImageView>(R.id.PlantImage)
            imageView.setImageBitmap(takenImage)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    //Get Plant text inputs

    //Upload Plant data to database
    //Refresh map database

}


