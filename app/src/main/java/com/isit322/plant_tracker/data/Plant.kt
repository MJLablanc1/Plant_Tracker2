package com.isit322.artworklist.data

class Plant : ArrayList<PlantItem>()

data class PlantItem(
    val plantName: String,
    val plantImg: String,
    val latitude: String,
    val longitude: String,
    val id: String
)