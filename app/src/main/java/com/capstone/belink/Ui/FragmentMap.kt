package com.capstone.belink.Ui

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.belink.Adapter.PlaceAdapter
import com.capstone.belink.Model.Data
import com.capstone.belink.Model.VisitLocation
import com.capstone.belink.Network.RetrofitClient
import com.capstone.belink.Network.RetrofitService
import com.capstone.belink.UIActivity.MainActivity
import com.capstone.belink.databinding.FragmentMapBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
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

    private val cal = Calendar.getInstance()

    //임시 date
    private val strStoreName:String = "스타벅스"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = FragmentMapBinding.inflate(inflater, container, false)
        val view = binding.root

        auto =(activity as MainActivity).getSharedPreferences("auto", Activity.MODE_PRIVATE)
        autoLogin=auto.edit()
        initRetrofit()
        init()
        //user 방문기록 가져오기
        val userId = auto.getString("userId", null).toString()
        supplementService.showPlace(userId)
                .enqueue(object : Callback<VisitLocation> {
                    override fun onResponse(call: Call<VisitLocation>, response: Response<VisitLocation>) {
                        val saveList: MutableList<Data> = response.body()!!.data
                        //Log.d("성공", response.body()!!.data[0].store.storeName)
                        println("~~~~~~~~~~~~~~~~~~~")
                        true
                    }

                    override fun onFailure(call: Call<VisitLocation>, t: Throwable) {
                        Log.d("실패", "$t")
                        println("~~~~~~~~fail~~~~~~~~~~~")
                    }
                })

        binding.calendarView.setOnDateChangeListener { calendarView, year, month, day ->
            val textView = String.format("%d / %d / %d", year, month + 1, day)
            // 선택한 날짜 띄우기
            Toast.makeText(mContext, textView, Toast.LENGTH_SHORT).show()
            //user 방문기록 가져오기
            val userId = auto.getString("userId", null).toString()

            supplementService.showPlace(userId)
                    .enqueue(object : Callback<VisitLocation> {
                        @RequiresApi(Build.VERSION_CODES.O)
                        override fun onResponse(call: Call<VisitLocation>, response: Response<VisitLocation>) {
                            val saveList: MutableList<Data> = response.body()!!.data
                            Log.d("성공", response.body()!!.data[0].store.createdAt.toString())

                            // 해당 날짜 데이터 받아오기
                            val visitList: MutableList<String> = arrayListOf()
                            for (i in saveList.indices) {
                                println("~~~~~~~~~~~~~~~~~~~")
                                var parserSDF = SimpleDateFormat("EEE MMM dd hh:mm:ss z yyyy", Locale.ENGLISH)
                                val strDate = saveList[i].store.createdAt
                                val date = parserSDF.parse(strDate.toString())
                                parserSDF = SimpleDateFormat("yyyy-MM-dd")
                                var dDate = parserSDF.format(date)
                                println(dDate)
                                println("~~~~~~~~~~~~~~~~~~~")
                                val stryear = dDate.slice(IntRange(0,3)).toInt()
                                val strmon = dDate.slice(IntRange(4,6)).toInt()
                                val strday = dDate.slice(IntRange(7,9)).toInt()
                                if(stryear==year && strmon==(month+1) && strday==day){
                                    visitList.add(saveList[i].store.storeName)
                                    println("~~~~~~~~~~~~~~~~~~~")
                                    println(saveList[i].store.storeName)
                                }
                            }
                            adaptPlace(visitList)
                            println(visitList)
                            true
                        }

                        override fun onFailure(call: Call<VisitLocation>, t: Throwable) {
                            Log.d("실패", "$t")
                            println("~~~~~~~~fail~~~~~~~~~~~")
                        }
                    })

        }

        return view
    }

    fun init(){
        val visitList: MutableList<String> = arrayListOf()
        visitList.add(strStoreName)
        binding.placeListView.layoutManager= LinearLayoutManager(mContext)
        adapter = PlaceAdapter(mContext!!)
        adapter.dataList=visitList
        binding.placeListView.adapter=adapter
    }

    private fun adaptPlace(visitList: MutableList<String>) {
        adapter = PlaceAdapter(mContext!!)
        adapter.dataList=visitList
        adapter.notifyDataSetChanged()
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