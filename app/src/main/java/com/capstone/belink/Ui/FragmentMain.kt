   package com.capstone.belink.Ui

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.belink.Adapter.SearchAdapter
import com.capstone.belink.Model.*
import com.capstone.belink.Network.RetrofitClient
import com.capstone.belink.Network.RetrofitService
import com.capstone.belink.UIActivity.MainActivity
import com.capstone.belink.Utils.*
import com.capstone.belink.databinding.FragmentMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit


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
        auto =(activity as MainActivity).getSharedPreferences("auto", Activity.MODE_PRIVATE)
        initRetrofit()
        setHasOptionsMenu(true)
        val dataList = getSearchPref(xContext,"searchContext")
        init(dataList)

        adaptSearch(dataList)

        return view
    }
       private  fun init(dataList:MutableList<SearchLocation>){
           binding.searchRecycler.layoutManager = LinearLayoutManager(mContext)
           adapter = SearchAdapter(xContext,dataList)
           binding.searchRecycler.adapter=adapter
       }

       private fun adaptSearch(dataList:MutableList<SearchLocation>) {
           // binding.searchRecycler.layoutManager = LinearLayoutManager(mContext)
           adapter = SearchAdapter(xContext,dataList)
           // 체크인
           adapter.setItemClickListener(object : SearchAdapter.OnItemClickListener{
               override fun onClick(v: View, position: Int) {
                   var storeId = dataList[position].id
                   val userId = auto.getString("userId", null).toString()
                   val teamRoomId = auto.getString("teamRoomId", null).toString()
                   // storeId 임시 값
                   // storeId = 8439657.toString()
                   println(storeId)
                   println(userId)
                   println(teamRoomId)
                   supplementService.nfcPushMsg(team_room = teamRoomId,userId = userId,storeId = storeId)
                           .enqueue(object : Callback<Map<String,Boolean>>{
                               override fun onResponse(call: Call<Map<String, Boolean>>, response: Response<Map<String, Boolean>>) {
                                   println("team_room = $teamRoomId,userId = $userId,storeId = $storeId")

                               }
                               override fun onFailure(call: Call<Map<String, Boolean>>, t: Throwable) {
                                   Log.d("실패","$t")
                               }
                           })
//                   supplementService.savePlace(userId,storeId)
//                           .enqueue(object : Callback<VisitedPlace>{
//                               override fun onResponse(call: Call<VisitedPlace>, response: Response<VisitedPlace>) {
//                                   if(response.message()=="OK") {
//                                       Log.d("성공", response.body()!!.toString())
//                                       val textView = "인증되었습니다."
//                                       Toast.makeText(xContext, textView, Toast.LENGTH_SHORT).show()
//                                   }
//                                   else
//                                       Log.d("실패",response.message())
//                                   true
//                               }
//                               override fun onFailure(call: Call<VisitedPlace>, t: Throwable) {
//                                   Log.d("실패","$t")
//                               }
//                           })
                   adapter.notifyDataSetChanged()
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


