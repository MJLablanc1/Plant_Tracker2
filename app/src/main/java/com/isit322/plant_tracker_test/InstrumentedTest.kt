package com.isit322.plant_tracker_test

import android.util.Log
import com.isit322.artworklist.api.ApiClient
import com.isit322.artworklist.data.Plant
import com.isit322.artworklist.utils.Config
import com.isit322.plant_tracker.Validation
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Call
import retrofit2.Response


@RunWith(JUnit4::class)
class InstrumentTest {
//    @Rule
//    @JvmField
//    val rule: ActivityScenarioRule<PlantView> = ActivityScenarioRule(PlantView::class.java)

    @Test
    fun enter_name_field() {
        val desc = "Sun flower"
        val result = Validation.returnPlantObjectName(desc)
        assertEquals(result, desc)

    }

    @Test
    fun enter_description_field() {
        val desc = "two"
        val result = Validation.returnPlantObjectDescription(desc)
        assertEquals(result, desc)
    }

    @Test
    fun plant_data_test() {
        val TAG = "plant_request"
        val get_plant_url = Config.BASE_URL + Config.GET_ENDPOINT
        val call = ApiClient.getUserApiService().getPlants(get_plant_url)
        call.enqueue(object:retrofit2.Callback<Plant> {
            override fun onResponse(call: Call<Plant>, response: Response<Plant>) {
                if (response.isSuccessful) {
                    Log.d(TAG, "Response is successful. Response: ${response.body()?.get(0)?.plantName}")
                    val  result =  response.body()?.get(0)?.plantName
                    val expected = "Sun flower"
                    assertEquals( expected, result)
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