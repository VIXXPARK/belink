package com.capstone.belink.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.capstone.belink.Model.FriendUserDTO
import com.capstone.belink.R


class FriendViewHolder(v:View) : RecyclerView.ViewHolder(v){

    val tv_friend_name = v.findViewById<TextView>(R.id.tv_friend_name)
}

class FriendAdapter(val context: Context):RecyclerView.Adapter<FriendViewHolder>(){
    var DataList = listOf<FriendUserDTO>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val cellForRow = LayoutInflater.from(context).inflate(R.layout.custom_friendlist,parent,false)
        return FriendViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        holder.tv_friend_name.setText(DataList[position].username)


    }

    override fun getItemCount(): Int {
        return DataList.size
    }

}