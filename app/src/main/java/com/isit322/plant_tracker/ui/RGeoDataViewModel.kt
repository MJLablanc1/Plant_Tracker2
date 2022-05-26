package com.isit322.plant_tracker.ui

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isit322.artworklist.api.ApiClient
import com.isit322.artworklist.data.Plant
import com.isit322.artworklist.utils.Config
import com.isit322.plant_tracker.data.RGeoData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class RGeoDataViewModel: ViewModel()  {
    var RGeoDataResponse = MutableLiveData<RGeoData>()
    val TAG = "RGeo_request"

    fun getRGeoData(latLong: String, context: Context) {

        viewModelScope.launch(Dispatchers.IO) {
            val search_rgeo_url = Config.GEO_ENDPOINT + Config.latLongPar + latLong + Config.apiKeyPar + Config.GEO_API_KEY
            val call = ApiClient.getUserApiService().getRGeoData(search_rgeo_url)
            call.enqueue(object:retrofit2.Callback<RGeoData> {
                override fun onResponse(call: Call<RGeoData>, response: Response<RGeoData>) {
                    if (response.isSuccessful) {
                        RGeoDataResponse.postValue(response.body())
                        Log.d(TAG, "Response is successful. Response: ${RGeoDataResponse.value}")
                    }
                    else {
                        Log.d(TAG, "Response is NOT successful.")
                    }

                }

                override fun onFailure(call: Call<RGeoData>, t: Throwable) {
                    Log.d(TAG, "Request failed. Message: {${t.message}")
                }
            })
        }

    }
}