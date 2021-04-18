package com.capstone.belink.Ui

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.capstone.belink.Adapter.ProfileData
import com.capstone.belink.UIActivity.MainActivity
import com.capstone.belink.Network.RetrofitClient
import com.capstone.belink.Network.RetrofitService
import com.capstone.belink.R
import com.capstone.belink.databinding.FragmentMapBinding

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


    val repository = arrayListOf(
        ProfileData(R.drawable.picachu, "팀플용"),
        ProfileData(R.drawable.picachu, "팀플용"),
        ProfileData(R.drawable.picachu, "팀플용"),
        ProfileData(R.drawable.picachu, "팀플용"),
        ProfileData(R.drawable.picachu, "팀플용")
    )



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = FragmentMapBinding.inflate(inflater,container,false)
        val view = binding.root

        auto =(activity as MainActivity).getSharedPreferences("auto",Activity.MODE_PRIVATE)
        autoLogin=auto.edit()

        initRetrofit()
        return view
    }


    private fun initRetrofit() {
        retrofit = RetrofitClient.getInstance(xContext)
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