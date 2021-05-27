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
import com.capstone.belink.Adapter.SearchAdapter
import com.capstone.belink.Model.Search
import com.capstone.belink.Model.SearchLocation
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

        initRetrofit()
        return view
    }

//       override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId){
//           R.id.action_search ->{
//               val searchView = item?.actionView as SearchView
//               searchView.setOnQueryTextListener(object :  SearchView.OnQueryTextListener{
//                   override fun onQueryTextSubmit(query: String): Boolean {
//                       // keyword : query
//                       supplementService.searchPlace(query)
//                               .enqueue(object : Callback<Search> {
//                                   override fun onResponse(call: Call<Search>, response: Response<Search>) {
//                                       val searchList= response.body()!!.data as MutableList<SearchLocation>
//                                       Log.d("성공ff",searchList[0].place_name)
//                                       for (i in 0 until searchList.size) {
//                                           searchList[i].distance=getDistance(gpsx,gpsy,searchList[i].x.toDouble(),searchList[i].y.toDouble()).toString()
//                                       }
//                                       searchList.sortBy { it.distance }
//                                       setSearchPref(xContext,"searchContext",searchList)
//                                       true
//                                       val searchListSub = getSearchPref(xContext,"searchContext")
//                                       Log.d("성공",searchListSub[0].distance)
//                                   }
//                                   override fun onFailure(call: Call<Search>, t: Throwable) {
//                                       Log.d("실패","$t")
//                                   }
//                               })
//                       return false
//                   }
//
//                   override fun onQueryTextChange(p0: String?): Boolean {
//                       return false
//                   }
//               })
//               true
//           }
//           else ->{
//               false
//           }
//       }
//       fun getDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Int {
//           val dLat = Math.toRadians(lat2 - lat1)
//           val dLon = Math.toRadians(lon2 - lon1)
//           val a = Math.sin(dLat / 2).pow(2.0) + Math.sin(dLon / 2).pow(2.0) * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
//           val c = 2 * Math.asin(Math.sqrt(a))
//           return (r * c).toInt()
//       }

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


