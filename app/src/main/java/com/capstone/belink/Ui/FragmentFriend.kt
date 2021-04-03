package com.capstone.belink.Ui

import android.content.Context
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
import com.capstone.belink.Model.FriendDao
import com.capstone.belink.Model.FriendUserDao
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

    private  var DataList= listOf(
            FriendUserDao("123","abc","01022222222"),
            FriendUserDao("143","abcd","01022222225"),
            FriendUserDao("113","abce","01022222224"),
            FriendUserDao("133","abcf","01022222223")

    )


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = FragmentFriendBinding.inflate(inflater,container,false)
        val view = binding.root
        (activity as AppCompatActivity).supportActionBar?.title="친구"
        initRetrofit()
        init()
        return view
    }



    private fun initRetrofit() {
        retrofit = RetrofitClient.getInstance()
        supplementService = retrofit.create(RetrofitService::class.java)
    }

    private fun init() {
        binding.friendRecycler.layoutManager = LinearLayoutManager(mContext)
        val adapter = FriendAdapter(xContext)
        adapter.DataList=DataList
        binding.friendRecycler.adapter=adapter

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