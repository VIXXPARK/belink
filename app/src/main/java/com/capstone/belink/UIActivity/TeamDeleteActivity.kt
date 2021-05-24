//package com.capstone.belink.UIActivity
//
//import android.app.Activity
//import android.content.SharedPreferences
//import android.os.Bundle
//import android.util.Log
//import android.view.Menu
//import android.view.MenuItem
//import android.view.View
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.capstone.belink.Adapter.AlarmAdapter
//import com.capstone.belink.Adapter.GroupAdapter
//import com.capstone.belink.Model.*
//import com.capstone.belink.Network.RetrofitClient
//import com.capstone.belink.Network.RetrofitService
//import com.capstone.belink.R
//import com.capstone.belink.Utils.getGroupPref
//import com.capstone.belink.Utils.getMemberPref
//import com.capstone.belink.Utils.setGroupPref
//import com.capstone.belink.databinding.ActivityAlarmBinding
//import com.capstone.belink.databinding.ActivityTeamDeleteBinding
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//import retrofit2.Retrofit
//
//class TeamDeleteActivity : AppCompatActivity() {
//    private var mBinding: ActivityTeamDeleteBinding?=null
//    private val binding get() = mBinding!!
//
//    private lateinit var retrofit: Retrofit
//    private lateinit var supplementService: RetrofitService
//
//    private lateinit var pref: SharedPreferences
//    private lateinit var prefEdit: SharedPreferences.Editor
//
//    private lateinit var adapter:GroupAdapter
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        mBinding= ActivityTeamDeleteBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        pref =getSharedPreferences("auto", Activity.MODE_PRIVATE)!!
//        prefEdit=pref.edit()
//
//        initRetrofit()
//
//        init()
//    }
//
//    private fun init() {
//        val groupList = getGroupPref(this,"groupContext")
//        deleteTeam(groupList)
//    }
//
//    private fun deleteTeam(groupList: MutableList<TeamRoom>){
//        binding.deleteGroupListView.layoutManager=LinearLayoutManager(this)
//        adapter = GroupAdapter(this,groupList)
//        adapter.setItemClickListener(object : GroupAdapter.OnItemClickListener{
//            override fun onClick(v: View, position: Int) {
//                val item = groupList[position]
//                supplementService.deleteTeam(item.id)
//                    .enqueue(object : Callback<Map<String,Boolean>>{
//                        override fun onResponse(call: Call<Map<String, Boolean>>, response: Response<Map<String, Boolean>>) {
//                            Log.d("성공",response.body().toString())
//                            adapter.notifyDataSetChanged()
//                            setResult(Activity.RESULT_OK)
//                            finish()
//                        }
//                        override fun onFailure(call: Call<Map<String, Boolean>>, t: Throwable) {
//                            Log.d("실패","$t")
//                        }
//
//                    })
//                adapter.notifyDataSetChanged()
//            }
//
//        })
//        binding.deleteGroupListView.adapter=adapter
//    }
//
//    private fun initRetrofit() {
//        retrofit= RetrofitClient.getInstance(this)
//        supplementService=retrofit.create(RetrofitService::class.java)
//    }
//
//
//    override fun onDestroy() {
//        mBinding=null
//        super.onDestroy()
//    }
//}