package com.isit322.plant_tracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.isit322.artworklist.adapters.AdapterRecycler
import com.isit322.artworklist.data.PlantItem
import com.isit322.artworklist.ui.PlantViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_plant_list.*

class PlantListActivity : AppCompatActivity() {
    lateinit var viewModel: PlantViewModel
    lateinit var adapterRecyclerView: AdapterRecycler
    var plantList: List<PlantItem>? = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plant_list)

        val actionBar = getSupportActionBar()
        if (actionBar!= null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
        }

        viewModel = ViewModelProvider(this).get(PlantViewModel::class.java)
        adapterRecyclerView = AdapterRecycler(plantList, this)
        recycler_view.adapter = adapterRecyclerView
        recycler_view.layoutManager = LinearLayoutManager(this)

        progress_bar.visibility = View.VISIBLE
        viewModel.getPlant(this)
        viewModel.plantResponse.observe(this) {
            if (!it.isEmpty()) {
                progress_bar.visibility = View.GONE
                linear_layout_recycler_view.visibility = View.VISIBLE
                plantList = it
                adapterRecyclerView.setData(plantList)
            } else {
                progress_bar.visibility = View.GONE
            }
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}