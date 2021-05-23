package com.capstone.belink.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.capstone.belink.Model.User
import com.capstone.belink.R
import com.capstone.belink.Utils.getMemberPref
import com.capstone.belink.Utils.setMemberPref


class FriendViewHolder(v:View) : RecyclerView.ViewHolder(v){

    val tv_friend_name = v.findViewById<TextView>(R.id.tv_friend_name)
    val check_in = v.findViewById<CheckBox>(R.id.check_in)


}

class FriendAdapter(val context: Context, private val dataList:MutableList<User>):RecyclerView.Adapter<FriendViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val cellForRow = LayoutInflater.from(context).inflate(R.layout.custom_friendlist,parent,false)
        return FriendViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        holder.tv_friend_name.text = dataList[position].username

        holder.itemView.setOnClickListener {
            Toast.makeText(context,position.toString(),Toast.LENGTH_SHORT).show()
        }

        holder.check_in.setOnClickListener(View.OnClickListener {
            if(holder.check_in.isChecked){
                val memberList=getMemberPref(context,"team")
                memberList[dataList[position].id]=true
                setMemberPref(context,"team",memberList)
            }else{
                val memberList=getMemberPref(context,"team")
                memberList[dataList[position].id]=false
                setMemberPref(context,"team",memberList)
            }
        })


    }

    override fun getItemCount(): Int {
        return dataList.size
    }

}