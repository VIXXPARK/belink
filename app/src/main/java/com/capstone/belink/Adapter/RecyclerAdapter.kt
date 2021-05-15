package com.capstone.belink.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.capstone.belink.Model.TeamRoom
import com.capstone.belink.R

class ProfileData(val profile:Int,val name:String)

class CustomViewHolder(v:View) : RecyclerView.ViewHolder(v){
    val iv_profile = v.findViewById<ImageView>(R.id.iv_profile)
    val tv_group_name = v.findViewById<TextView>(R.id.tv_group_name)
}

class RecyclerAdapter(val context: Context):RecyclerView.Adapter<CustomViewHolder>(){
    var dataList:MutableList<TeamRoom> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val cellForRow = LayoutInflater.from(context).inflate(R.layout.custom_grouplist,parent,false)
        return CustomViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.iv_profile.setImageResource(R.drawable.picachu)
        holder.tv_group_name.text = dataList[position].teamName

    }

    override fun getItemCount(): Int {
        return dataList.size
    }

}