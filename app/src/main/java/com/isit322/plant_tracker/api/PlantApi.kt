package com.isit322.artworklist.api

import com.isit322.artworklist.data.Plant
import com.isit322.plant_tracker.data.RGeoData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface PlantApi {
    @GET
    fun getPlants(@Url url: String): Call<Plant>

    @GET
    fun getRGeoData(@Url url: String): Call<RGeoData>
}