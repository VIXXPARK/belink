package com.capstone.belink.Ui

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.belink.Adapter.FriendAdapter
import com.capstone.belink.Model.FriendUser
import com.capstone.belink.Network.RetrofitClient
import com.capstone.belink.Network.RetrofitService
import com.capstone.belink.UIActivity.TeamActivity
import com.capstone.belink.Utils.getStringArraySaved
import com.capstone.belink.databinding.FragmentFriendBinding
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

    private lateinit var adapter: FriendAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = FragmentFriendBinding.inflate(inflater,container,false)
        val view = binding.root

        auto =(activity as TeamActivity).getSharedPreferences("auto", Activity.MODE_PRIVATE)
        initRetrofit()
        init()



        return view
    }

    private fun adaptFriend(dataList:MutableList<FriendUser>) {
        binding.friendRecycler.layoutManager = LinearLayoutManager(mContext)
        adapter = FriendAdapter(xContext,dataList)
        binding.friendRecycler.adapter=adapter

    }


    private fun initRetrofit() {
        retrofit = RetrofitClient.getInstance(xContext)
        supplementService = retrofit.create(RetrofitService::class.java)
    }


    private fun init() {
        val id = auto.getString("userId","")!!
        val userList= getStringArraySaved(xContext,"contact")
        adaptFriend(userList)
        if(userList.isEmpty()){
            (activity as TeamActivity).replaceFragment(FragmentEmpty())
        }
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