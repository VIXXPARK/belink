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
import com.capstone.belink.Adapter.PlaceAdapter
import com.capstone.belink.Adapter.RecyclerAdapter
import com.capstone.belink.Model.*
import com.capstone.belink.Network.RetrofitClient
import com.capstone.belink.Network.RetrofitService
import com.capstone.belink.UIActivity.MainActivity
import com.capstone.belink.databinding.FragmentMapBinding
import okhttp3.internal.Internal.instance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.time.LocalDateTime
import java.time.Month
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
    private val sdf: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")

    //임시 date
    private val strDate:String = "2021-05-26T07:36:40.000Z"
    private val strStoreName:String = "스타벅스"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = FragmentMapBinding.inflate(inflater, container, false)
        val view = binding.root

        auto =(activity as MainActivity).getSharedPreferences("auto", Activity.MODE_PRIVATE)
        autoLogin=auto.edit()

        init()
        //user 방문기록 가져오기
        val userId = auto.getString("userId", null).toString()
//        supplementService.showPlace(userId)
//                .enqueue(object : Callback<Map<Data, Boolean>> {
//                    override fun onResponse(call: Call<Map<Data, Boolean>>, response: Response<Map<Data, Boolean>>) {
//                        val saveList: MutableList<Store> = response.body()?.keys as MutableList<Store>
//                        Log.d("성공", saveList[0].storeName)
//                        println("~~~~~~~~~~~~~~~~~~~")
//                        println(saveList[0].storeName)
//                        true
//                    }
//
//                    override fun onFailure(call: Call<Map<Data, Boolean>>, t: Throwable) {
//                        Log.d("실패", "$t")
//                        println("~~~~~~~~fail~~~~~~~~~~~")
//                    }
//                })

        binding.calendarView.setOnDateChangeListener { calendarView, year, month, day ->
            val textView = String.format("%d / %d / %d", year, month + 1, day)
            // 선택한 날짜 띄우기
            Toast.makeText(mContext, textView, Toast.LENGTH_SHORT).show()

            // 해당 날짜 데이터 받아오기
            val visitList: MutableList<String> = arrayListOf()
            //  for (i in data.indices){
                // 날짜 변환
                val stryear = strDate.subSequence(0,4).toString().toInt()
                val strmon = strDate.subSequence(5,7).toString().toInt()
                val strday = strDate.subSequence(8,10).toString().toInt()
            //    if(stryear==year && strmon==(month+1) && strday==day){
                    visitList.add(strStoreName)
            //    }
            //  }
            adaptPlace(visitList)

        }
        initRetrofit()
        return view
    }
    fun init(){
        val visitList: MutableList<String> = arrayListOf()
        visitList.add(strStoreName)
        //  binding.placeListView.layoutManager= LinearLayoutManager(mContext)
        adapter = PlaceAdapter(mContext!!)
        adapter.dataList=visitList
        binding.placeListView.adapter=adapter
    }

    private fun adaptPlace(visitList:MutableList<String>) {
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