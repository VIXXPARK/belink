package com.capstone.belink.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.capstone.belink.Model.TeamRoom
import com.capstone.belink.R

class DeleteViewHolder(v: View) : RecyclerView.ViewHolder(v){

    val group_name = v.findViewById<TextView>(R.id.tv_delete_group_name)
}

class DeleteAdapter(val context: Context, private val dataList:MutableList<TeamRoom>): RecyclerView.Adapter<DeleteViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeleteViewHolder {
        val cellForRow = LayoutInflater.from(context).inflate(R.layout.custom_delete,parent,false)
        return DeleteViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: DeleteViewHolder, position: Int) {
        holder.group_name.text = dataList[position].teamName

        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it,position)
        }
    }

    interface OnItemClickListener{
        fun onClick(v: View, position: Int)
    }
    private lateinit var itemClickListener: OnItemClickListener

    fun setItemClickListener(itemClickListener: OnItemClickListener){
        this.itemClickListener = itemClickListener
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

}