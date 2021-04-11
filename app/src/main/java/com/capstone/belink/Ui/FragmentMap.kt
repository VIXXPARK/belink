package com.capstone.belink.Ui

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.capstone.belink.UIActivity.MainActivity
import com.capstone.belink.Model.User
import com.capstone.belink.Model.Success
import com.capstone.belink.Network.RetrofitClient
import com.capstone.belink.Network.RetrofitService
import com.capstone.belink.databinding.FragmentMapBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class FragmentMap:Fragment() {
    private var mBinding:FragmentMapBinding?=null
    private val binding get() = mBinding!!

    private var mContext:Context?=null
    private val xContext get() = mContext!!

    private lateinit var retrofit : Retrofit
    private lateinit var supplementService : RetrofitService

    private lateinit var auto: SharedPreferences
    private lateinit var autoLogin: SharedPreferences.Editor

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = FragmentMapBinding.inflate(inflater,container,false)
        val view = binding.root

        auto =(activity as MainActivity).getSharedPreferences("auto",Activity.MODE_PRIVATE)
        autoLogin=auto.edit()

        initRetrofit()
        return view
    }

    private fun connectUpdateInfo(phoneNumber: String, name: String,id:String) {
        val user = User(id,phoneNumber,name)
        supplementService.editUser(user).enqueue(object :Callback<Success>{
            override fun onResponse(call: Call<Success>, response: Response<Success>) {
                Log.d("success",response.body().toString())
            }

            override fun onFailure(call: Call<Success>, t: Throwable) {
                Log.d("fail","$t")
            }

        })

    }

    private fun initRetrofit() {
        retrofit = RetrofitClient.getInstance()
        supplementService = retrofit.create(RetrofitService::class.java)
    }



    override fun onAttach(context: Context) {
        mContext=context
        super.onAttach(context)
    }
    override fun onDestroy() {
        mBinding=null
        super.onDestroy()
    }
}