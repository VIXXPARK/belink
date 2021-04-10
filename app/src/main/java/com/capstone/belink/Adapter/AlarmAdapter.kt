package com.capstone.belink.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstone.belink.R


class AlarmAdapter(val context:Context, val DataList:List<Message>): RecyclerView.Adapter<AlarmViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        val info = LayoutInflater.from(context).inflate(R.layout.custom_alramlist,parent,false)
        return AlarmViewHolder(info)
    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        holder.tv_alarm_value.setText(DataList[position].msg)
    }

    override fun getItemCount(): Int {
        return DataList.size
    }

}