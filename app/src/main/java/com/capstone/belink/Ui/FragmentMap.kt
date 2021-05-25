package com.capstone.belink.Ui

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.belink.Adapter.GroupAdapter
import com.capstone.belink.Adapter.PlaceAdapter
import com.capstone.belink.Adapter.ProfileData
import com.capstone.belink.Adapter.RecyclerAdapter
import com.capstone.belink.Model.Place
import com.capstone.belink.Model.TeamRoom
import com.capstone.belink.Network.RetrofitClient
import com.capstone.belink.Network.RetrofitService
import com.capstone.belink.R
import com.capstone.belink.UIActivity.MainActivity
import com.capstone.belink.Utils.getGroupPref
import com.capstone.belink.databinding.FragmentMapBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.util.*

class FragmentMap:Fragment() {
    private var mBinding:FragmentMapBinding?=null
    private val binding get() = mBinding!!

    private var mContext:Context?=null
    private val xContext get() = mContext!!

    private lateinit var retrofit : Retrofit
    private lateinit var supplementService : RetrofitService

    private lateinit var auto: SharedPreferences
    private lateinit var autoLogin: SharedPreferences.Editor

    private lateinit var adapter: PlaceAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = FragmentMapBinding.inflate(inflater, container, false)
        val view = binding.root

        auto =(activity as MainActivity).getSharedPreferences("auto", Activity.MODE_PRIVATE)
        autoLogin=auto.edit()

        binding.calendarView.setOnDateChangeListener { calendarView, year, month, day ->
            Toast.makeText(mContext, "$year 년 $month 월 $day 일", Toast.LENGTH_SHORT).show()
            adaptPlace()

        }
        initRetrofit()
        return view
    }
    private fun adaptPlace() { // storename 가져오기
        val visitList: MutableList<Place> = arrayListOf()
        val obj1 = Place(storeName = "스타벅스")
        val obj2 = Place(storeName = "투썸플레이스")
        val obj3 = Place(storeName = "서브웨이")
        visitList.add(obj1)
        visitList.add(obj2)
        visitList.add(obj3)

        binding.placeListView.layoutManager= LinearLayoutManager(mContext)
        adapter = PlaceAdapter(mContext!!,visitList)
        binding.placeListView.adapter=adapter
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