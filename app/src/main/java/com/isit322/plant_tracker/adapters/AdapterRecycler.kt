package com.isit322.artworklist.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.isit322.artworklist.data.PlantItem
import com.isit322.plant_tracker.R
import kotlinx.android.synthetic.main.row_plant_item_adapter_list.view.*

class AdapterRecycler(var mList: List<PlantItem>?, var mContext: Context): RecyclerView.Adapter<AdapterRecycler.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(mContext).inflate(R.layout.row_plant_item_adapter_list, parent, false)
        var myViewHolder = MyViewHolder(view)
        return myViewHolder
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var plant = mList?.get(position)
        if (plant != null) {
            holder.bind(plant)
        }
    }

    override fun getItemCount(): Int {
        return mList?.size!!
    }

    fun setData(mList: List<PlantItem>?) {
        this.mList = mList
        notifyDataSetChanged()
    }

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(result: PlantItem) {
            itemView.text_view_plant_name.text = result.plantName
            itemView.text_view_plant_img.text = result.plantImg
            itemView.text_view_location.text = result.location

        }
    }
}