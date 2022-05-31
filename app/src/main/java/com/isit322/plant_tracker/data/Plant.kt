package com.isit322.artworklist.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

class Plant : ArrayList<PlantItem>()

@Parcelize
data class PlantItem(
    val plantName: String,
    val plantImg: String,
    val location: String,
    val id: String
): Parcelable

