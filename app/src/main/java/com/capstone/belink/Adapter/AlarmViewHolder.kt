package com.capstone.belink.Adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.capstone.belink.R

class Message(val msg:String)

class AlarmViewHolder(v: View): RecyclerView.ViewHolder(v) {
    val tv_alarm_value = v.findViewById<TextView>(R.id.tv_alarm_value)
}
