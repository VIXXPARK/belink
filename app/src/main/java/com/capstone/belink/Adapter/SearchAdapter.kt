package com.capstone.belink.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.capstone.belink.Model.SearchLocation
import com.capstone.belink.Model.TeamRoom
import com.capstone.belink.R


class SearchViewHolder(v:View) : RecyclerView.ViewHolder(v){

    val group_name = v.findViewById<TextView>(R.id.tv_search_name)
}

class SearchAdapter(val context: Context, var dataList:MutableList<SearchLocation>):RecyclerView.Adapter<SearchViewHolder>(){




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val cellForRow = LayoutInflater.from(context).inflate(R.layout.custom_search,parent,false)
        return SearchViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.group_name.text = dataList[position].place_name

        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it,position)
        }
    }

    interface OnItemClickListener{
        fun onClick(v:View,position: Int)
    }
    private lateinit var itemClickListener: OnItemClickListener

    fun setItemClickListener(itemClickListener: OnItemClickListener){
        this.itemClickListener = itemClickListener
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

}