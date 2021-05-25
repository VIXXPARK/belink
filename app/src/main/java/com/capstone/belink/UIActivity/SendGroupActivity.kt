package com.capstone.belink.UIActivity

import android.app.Activity
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.belink.Adapter.GroupAdapter
import com.capstone.belink.Model.TeamRoom
import com.capstone.belink.Network.RetrofitClient
import com.capstone.belink.Network.RetrofitService
import com.capstone.belink.Utils.getGroupPref
import com.capstone.belink.databinding.ActivitySendGroupBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class SendGroupActivity : AppCompatActivity() {

    private var mBinding:ActivitySendGroupBinding?=null
    private val binding get() = mBinding!!

    private lateinit var retrofit: Retrofit
    private lateinit var supplementService: RetrofitService

    private var storeId=""

    private lateinit var pref: SharedPreferences

    private lateinit var adapter:GroupAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySendGroupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRetrofit()
        storeId = intent.getStringExtra("storeId")!!
        pref =getSharedPreferences("auto", Activity.MODE_PRIVATE)!!
        init()
        Toast.makeText(this,pref.getString("userId","")!!,Toast.LENGTH_SHORT).show()
    }

    private fun init() {
        val groupList = getGroupPref(this,"groupContext")
        adaptGroup(groupList)
    }

    private fun adaptGroup(groupList: MutableList<TeamRoom>) {
        binding.finalGroupListView.layoutManager=LinearLayoutManager(this)
        adapter = GroupAdapter(this,groupList)
        adapter.setItemClickListener(object : GroupAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                val item = groupList[position]
                retrofitNfcPushMsg(item)
                adapter.notifyDataSetChanged()
            }
        })
        binding.finalGroupListView.adapter=adapter
    }

    private fun retrofitNfcPushMsg(item: TeamRoom) {
        supplementService.nfcPushMsg(team_room = item.id,userId = pref.getString("userId","")!!,storeId = storeId)
            .enqueue(object : Callback<Map<String,Boolean>>{
                override fun onResponse(call: Call<Map<String, Boolean>>, response: Response<Map<String, Boolean>>) {
                    println("team_room = ${item.id},userId = ${pref.getString("userId","")!!},storeId = $storeId")
                    setResult(Activity.RESULT_OK)
                    finish()
                }
                override fun onFailure(call: Call<Map<String, Boolean>>, t: Throwable) {
                    Log.d("실패","$t")
                }
            })
    }



    private fun initRetrofit() {
        retrofit= RetrofitClient.getInstance(this)
        supplementService=retrofit.create(RetrofitService::class.java)
    }
}