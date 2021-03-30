package com.capstone.belink.Ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.belink.Adapter.CustomFriendAdapter
import com.capstone.belink.Adapter.FriendData
import com.capstone.belink.databinding.FragmentFriendBinding

class FragmentFriend:Fragment() {
    private var mBinding:FragmentFriendBinding?=null
    private val binding get() = mBinding!!

    private var mContext:Context?=null
    private val xContext get() = mContext!!

    private var DataList = listOf(
        FriendData("박수한"),
        FriendData("김현진"),
        FriendData("김민섭"),
        FriendData("김경재")
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = FragmentFriendBinding.inflate(inflater,container,false)
        val view = binding.root

        init()

        return view
    }

    private fun init() {
        binding.friendRecycler.layoutManager = LinearLayoutManager(mContext)
        val adapter = CustomFriendAdapter(xContext)
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