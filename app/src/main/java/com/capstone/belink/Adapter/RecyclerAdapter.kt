package com.capstone.belink.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.capstone.belink.R

class ProfileData(val profile:Int,val name:String,val groupMember:String,val updatedDt: String)

class CustomViewHolder(v:View) : RecyclerView.ViewHolder(v){
    val iv_profile = v.findViewById<ImageView>(R.id.iv_profile)
    val tv_group_name = v.findViewById<TextView>(R.id.tv_group_name)
    val tv_group_total_name = v.findViewById<TextView>(R.id.tv_group_total_name)
    val tv_group_date = v.findViewById<TextView>(R.id.tv_group_date)
}

class RecyclerAdapter(val context: Context):RecyclerView.Adapter<CustomViewHolder>(){
    var DataList= listOf<ProfileData>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val cellForRow = LayoutInflater.from(context).inflate(R.layout.custom_grouplist,parent,false)
        return CustomViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.iv_profile.setImageResource(DataList[position].profile)
        holder.tv_group_name.setText(DataList[position].name)
        holder.tv_group_total_name.setText(DataList[position].groupMember)
        holder.tv_group_date.setText(DataList[position].updatedDt)
    }

    override fun getItemCount(): Int {
        return DataList.size
    }

}