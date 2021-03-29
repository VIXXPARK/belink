package com.capstone.belink

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text
import java.util.*
import kotlin.collections.ArrayList

class FriendData(val name:String)

class CustomFriendViewHolder(v:View) : RecyclerView.ViewHolder(v){

    val tv_friend_name = v.findViewById<TextView>(R.id.tv_friend_name)
}

class CustomFriendAdapter(val context: Context):RecyclerView.Adapter<CustomFriendViewHolder>(){
    var DataList = listOf<FriendData>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomFriendViewHolder {
        val cellForRow = LayoutInflater.from(context).inflate(R.layout.custom_friendlist,parent,false)
        return CustomFriendViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: CustomFriendViewHolder, position: Int) {
        holder.tv_friend_name.setText(DataList[position].name)


    }

    override fun getItemCount(): Int {
        return DataList.size
    }

}