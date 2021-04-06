package com.capstone.belink

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.belink.Adapter.FriendAdapter
import com.capstone.belink.Model.FriendListDTO
import com.capstone.belink.Model.FriendUserDTO
import com.capstone.belink.Model.SignDTO
import com.capstone.belink.Model.successIntDTO
import com.capstone.belink.Network.RetrofitClient
import com.capstone.belink.Network.RetrofitService
import com.capstone.belink.databinding.ActivityTeamBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class TeamActivity : AppCompatActivity() {
    private var mBinding:ActivityTeamBinding?=null
    private val binding get() = mBinding!!

    private lateinit var retrofit : Retrofit
    private lateinit var supplementService : RetrofitService

    private lateinit var pref: SharedPreferences
    private lateinit var prefEdit: SharedPreferences.Editor

    private var mContext: Context?=null
    private val xContext get() = mContext!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityTeamBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRetrofit()

        pref =getSharedPreferences("auto", Activity.MODE_PRIVATE)!!
        prefEdit=pref.edit()

        binding.btnTeamNext.setOnClickListener {
            supplementService.makeTeam(binding.etTeamName.text.toString()).enqueue(object : Callback<SignDTO>{
                override fun onResponse(call: Call<SignDTO>, response: Response<SignDTO>) {
                    Log.d("makeTeam","success")
                }

                override fun onFailure(call: Call<SignDTO>, t: Throwable) {
                    Log.d("fail","$t")
                }

            })
        }
    }



    private fun initRetrofit() {
        retrofit = RetrofitClient.getInstance()
        supplementService = retrofit.create(RetrofitService::class.java)
    }
}