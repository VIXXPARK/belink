package com.capstone.belink.UIActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.belink.Adapter.AlarmAdapter
import com.capstone.belink.Adapter.Message
import com.capstone.belink.databinding.ActivityAlarmBinding

class AlarmActivity : AppCompatActivity() {
    private var mBinding:ActivityAlarmBinding?=null
    private val binding get() = mBinding!!

    val DataList= listOf<Message>(
        Message("오늘 너의 친구가 병에 걸렸다는데? 어서 가는게 좋을걸? "),
        Message("그래도 안가? "),
        Message("다른 친구들도 걸렸네 ... "),
        Message("빨리 가는 것을 추천해")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding= ActivityAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.viewAlarm.layoutManager=LinearLayoutManager(this)
        val adapter = AlarmAdapter(this,DataList)
        binding.viewAlarm.adapter=adapter

    }

    override fun onDestroy() {
        mBinding=null
        super.onDestroy()
    }
}