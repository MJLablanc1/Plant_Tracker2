package com.isit322.artworklist.data

class Plant : ArrayList<PlantItem>()

data class PlantItem(
    val plantName: String,
    val plantImg: String,
    val location: String
)