package com.capstone.belink.Ui

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.belink.Adapter.FriendAdapter
import com.capstone.belink.MainActivity
import com.capstone.belink.Model.FriendListDTO
import com.capstone.belink.Model.FriendUserDTO
import com.capstone.belink.Network.RetrofitClient
import com.capstone.belink.Network.RetrofitService
import com.capstone.belink.databinding.FragmentFriendBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class FragmentFriend:Fragment() {
    private var mBinding:FragmentFriendBinding?=null
    private val binding get() = mBinding!!

    private var mContext:Context?=null
    private val xContext get() = mContext!!


    private lateinit var retrofit : Retrofit
    private lateinit var supplementService : RetrofitService

    private lateinit var auto: SharedPreferences
    private lateinit var autoLogin: SharedPreferences.Editor



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = FragmentFriendBinding.inflate(inflater,container,false)
        val view = binding.root

        auto =(activity as MainActivity).getSharedPreferences("auto", Activity.MODE_PRIVATE)
        autoLogin=auto.edit()
        (activity as AppCompatActivity).supportActionBar?.title="친구"
        initRetrofit()
        init()

        return view
    }

    private fun adaptFriend(DataList:MutableList<FriendUserDTO>) {
        binding.friendRecycler.layoutManager = LinearLayoutManager(mContext)
        val adapter = FriendAdapter(xContext,DataList)
        binding.friendRecycler.adapter=adapter
    }


    private fun initRetrofit() {
        retrofit = RetrofitClient.getInstance()
        supplementService = retrofit.create(RetrofitService::class.java)
    }


    private fun init() {
        val id = auto.getString("userId","")!!

        supplementService.getMyFriend(id,false).enqueue(object : Callback<FriendListDTO>{
            override fun onResponse(call: Call<FriendListDTO>, response: Response<FriendListDTO>) {
                val freind= response.body()?.data
                val DataList=mutableListOf<FriendUserDTO>()
                for(i in freind!!.indices){
                    val id = freind[i].myFriendUser.id
                    val phNum = freind[i].myFriendUser.phNum
                    val username = freind[i].myFriendUser.username
                    DataList.add(FriendUserDTO(id,username,phNum))
                    Log.d("$i","$id, $phNum, $username")
                }
                adaptFriend(DataList)
            }

            override fun onFailure(call: Call<FriendListDTO>, t: Throwable) {
                Log.d("status","fail")
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