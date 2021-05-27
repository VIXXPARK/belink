   package com.capstone.belink.Ui

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.belink.Adapter.FriendAdapter
import com.capstone.belink.Adapter.GroupAdapter
import com.capstone.belink.Adapter.SearchAdapter
import com.capstone.belink.Model.*
import com.capstone.belink.Network.RetrofitClient
import com.capstone.belink.Network.RetrofitService
import com.capstone.belink.R
import com.capstone.belink.UIActivity.MainActivity
import com.capstone.belink.Utils.*
import com.capstone.belink.databinding.FragmentMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Field
import kotlin.math.pow


   class FragmentMain : Fragment() {
    private var mBinding:FragmentMainBinding?=null
    private val binding get() = mBinding!!

    private var mContext:Context?=null
    private val xContext get() = mContext!!

    private var mActivity: Activity?=null
    private val xActivity get() = mActivity!!

       private lateinit var retrofit : Retrofit
       private lateinit var supplementService : RetrofitService

       private lateinit var auto: SharedPreferences

       private lateinit var adapter: SearchAdapter

       //gps관련 변수
       private val r = 6372.8 * 1000
       var gpsx=127.0
       var gpsy=30.0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = FragmentMainBinding.inflate(inflater, container, false)
        val view = binding.root
        (activity as AppCompatActivity).supportActionBar?.title="개인"

        setHasOptionsMenu(true)
        // init() 초기 예측 목록 띄우기
        // 액션바에서 검색 기능 실행 시
        val searchList = getSearchPref(xContext,"searchContext")
        adaptSearch(searchList)
        initRetrofit()
        return view
    }
       private fun adaptSearch(dataList:MutableList<SearchLocation>) {
           binding.searchRecycler.layoutManager = LinearLayoutManager(mContext)
           adapter = SearchAdapter(xContext,dataList)
           // 체크인
           adapter.setItemClickListener(object : SearchAdapter.OnItemClickListener{
               override fun onClick(v: View, position: Int) {
                   val storeId = dataList[position].id
                   val userId = auto.getString("userId", null).toString()
                   supplementService.savePlace(userId,storeId)
                           .enqueue(object : Callback<Map<PlaceData,Boolean>>{
                               override fun onResponse(call: Call<Map<PlaceData,Boolean>>, response: Response<Map<PlaceData,Boolean>>) {
                                   Log.d("성공",response.body()!!.values.toString())
                                   true
                               }
                               override fun onFailure(call: Call<Map<PlaceData,Boolean>>, t: Throwable) {
                                   Log.d("실패","$t")
                               }
                           })
                   //adapter.notifyDataSetChanged()
               }
           })
           binding.searchRecycler.adapter=adapter
       }

    override fun onAttach(context: Context) {
        mContext=context
        mActivity = activity as MainActivity?
        super.onAttach(context)
    }


       override fun onDestroy() {
    mBinding=null
    super.onDestroy()
    }
       private fun initRetrofit() {
           retrofit = RetrofitClient.getInstance(xContext)
           supplementService = retrofit.create(RetrofitService::class.java)
       }

}


