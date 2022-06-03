package com.isit322.artworklist.ui

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isit322.artworklist.api.ApiClient
import com.isit322.artworklist.data.Plant
import com.isit322.artworklist.data.PlantItem
import com.isit322.artworklist.utils.Config
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class PlantViewModel: ViewModel() {
    var plantResponse = MutableLiveData<List<PlantItem>>()
    var plantObjectResponse = MutableLiveData<PlantItem>()

    val TAG = "plant_request"

    fun getPlant(context: Context) {

        viewModelScope.launch(Dispatchers.IO) {
            val get_plant_url = Config.BASE_URL + Config.GET_ENDPOINT
            val call = ApiClient.getUserApiService().getPlants(get_plant_url)
            call.enqueue(object:retrofit2.Callback<Plant> {
                override fun onResponse(call: Call<Plant>, response: Response<Plant>) {
                    if (response.isSuccessful) {
                        plantResponse.postValue(response.body())
                        Log.d(TAG, "Response is successful. Response: ${plantResponse.value}")
                    }
                    else {
                        Log.d(TAG, "Response is NOT successful.")
                    }

                }

                override fun onFailure(call: Call<Plant>, t: Throwable) {
                    Log.d(TAG, "Request failed. Message: {${t.message}")
                }
            })
        }
    }

    fun postPlant(context: Context, plantObject: PlantItem) {

        viewModelScope.launch(Dispatchers.IO) {
            val post_plant_url = Config.BASE_URL + Config.POST_ENDPOINT
            val call = ApiClient.getUserApiService().postPlant(post_plant_url, plantObject)
            call.enqueue(object:retrofit2.Callback<PlantItem> {
                override fun onResponse(call: Call<PlantItem>, response: Response<PlantItem>) {
                    if (response.isSuccessful) {
                        plantObjectResponse.postValue(response.body())
                        Log.d(TAG, "Response is successful. Response: ${response.isSuccessful}")
                    }
                    else {
                        Log.d(TAG, "Response is NOT successful.")
                    }

                }

                override fun onFailure(call: Call<PlantItem>, t: Throwable) {
                    Log.d(TAG, "Request failed. Message: {${t.message}")
                }
            })
        }
    }


}