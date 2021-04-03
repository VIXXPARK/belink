package com.capstone.belink.Ui

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.capstone.belink.MainActivity
import com.capstone.belink.Model.FriendDao
import com.capstone.belink.Model.SignDao
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = FragmentMapBinding.inflate(inflater,container,false)
        val view = binding.root
        initRetrofit()
        (activity as AppCompatActivity).supportActionBar?.title="방문장소"



        binding.btnConnect.setOnClickListener {
            var pref =(activity as MainActivity).getPreferences(Activity.MODE_PRIVATE)
            println(pref.getString("userId","default"))
            getFriendList()
        }
        return view
    }

    private fun initRetrofit() {
        retrofit = RetrofitClient.getInstance()
        supplementService = retrofit.create(RetrofitService::class.java)
    }

    fun getFriendList() {
        var pref =(activity as MainActivity).getPreferences(Activity.MODE_PRIVATE)

        supplementService.getMyFriend(pref.getString("userId","01011111111")!!).enqueue(object :Callback<List<FriendDao>>{
            override fun onResponse(call: Call<List<FriendDao>>, response: Response<List<FriendDao>>) {
                Log.d("status",response.message())
            }

            override fun onFailure(call: Call<List<FriendDao>>, t: Throwable) {

            }


        })
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