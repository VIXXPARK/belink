package com.capstone.belink.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.capstone.belink.Model.Store
import com.capstone.belink.Model.TeamRoom
import com.capstone.belink.R


class PlaceViewHolder(v:View) : RecyclerView.ViewHolder(v){

    val place_name = v.findViewById<TextView>(R.id.tv_place_name)
}

class PlaceAdapter(val context: Context):RecyclerView.Adapter<PlaceViewHolder>(){
    var dataList:MutableList<String> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val cellForRow = LayoutInflater.from(context).inflate(R.layout.custom_place,parent,false)
        return PlaceViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        holder.place_name.text = dataList[position]

        holder.apply {

        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}