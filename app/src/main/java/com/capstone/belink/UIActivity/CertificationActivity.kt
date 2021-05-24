package com.capstone.belink.UIActivity

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.capstone.belink.Model.Member
import com.capstone.belink.Model.Sign
import com.capstone.belink.Model.Team
import com.capstone.belink.Model.TeamRoom
import com.capstone.belink.Network.RetrofitClient
import com.capstone.belink.Network.RetrofitService
import com.capstone.belink.Utils.getGroupPref
import com.capstone.belink.Utils.setGroupPref
import com.capstone.belink.databinding.ActivityCertificationBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class CertificationActivity : AppCompatActivity() {
    private var mBinding:ActivityCertificationBinding?=null
    private val binding get() = mBinding!!

    private lateinit var retrofit : Retrofit
    private lateinit var supplementService : RetrofitService

    private lateinit var auto: SharedPreferences
    private lateinit var autoLogin: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding= ActivityCertificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSharedPreferences()
        initRetrofit()
        btnCertificationListener()
        btnSignListener()

    }

    private fun btnCertificationListener() {
        var phNum = auto.getString("inputPhone","")!!
        val first=phNum.slice(IntRange(0,2))
        val second = phNum.slice(IntRange(4,7))
        val third = phNum.slice(IntRange(9,12))
        phNum=first+second+third
        binding.btnSendNumber.setOnClickListener {
            supplementService.sendMsg(to=phNum).enqueue(object : Callback<Map<String,Boolean>>{
                override fun onResponse(call: Call<Map<String, Boolean>>, response: Response<Map<String, Boolean>>) {
                    if(response.message()=="OK"){
                        Toast.makeText(this@CertificationActivity,"전송했습니다.",Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<Map<String, Boolean>>, t: Throwable) {
                }
            })
        }

        binding.btnCertCheck.setOnClickListener {
            val content = binding.etCertification.text.toString()
            supplementService.sendCode(phNum=phNum,certNum = content).enqueue(object : Callback<Map<String,Boolean>>{
                override fun onResponse(call: Call<Map<String, Boolean>>, response: Response<Map<String, Boolean>>) {
                    if (response.message()=="OK"){
                        binding.btnCertification.isEnabled=true
                    }
                }
                override fun onFailure(call: Call<Map<String, Boolean>>, t: Throwable) {
                }
            })
        }
    }

    private fun setSharedPreferences() {
        auto =getSharedPreferences("auto", Activity.MODE_PRIVATE)!!
        autoLogin=auto.edit()
    }

    private fun initRetrofit() {
        retrofit = RetrofitClient.getInstance(this)
        supplementService = retrofit.create(RetrofitService::class.java)
    }

    private fun btnSignListener() {
        val TOKEN = auto.getString("token","")!!
        val Phone = auto.getString("inputPhone","")!!
        val name = auto.getString("inputName","")!!
        binding.btnCertification.setOnClickListener {
                supplementService.registerUser(Phone, name,TOKEN).enqueue(object : Callback<Sign> {
                    override fun onResponse(call: Call<Sign>, response: Response<Sign>) {
                        if(response.message()=="Created"){
                            val userId = response.body()!!.data.id
                            var teamList: MutableList<Member> = ArrayList()
                            retrofitMakeTeam(response,teamList,userId)
                        }
                    }
                    override fun onFailure(call: Call<Sign>, t: Throwable) {
                        Log.d("fail", "$t")
                    }
                })
            }
    }

    private fun retrofitMakeTeam(response: Response<Sign>, teamList: MutableList<Member>, userId: String) {
        supplementService.makeTeam(response.body()!!.data.username).enqueue(object : Callback<Team> {
            override fun onResponse(call: Call<Team>, response: Response<Team>) {
                if (response.message() == "Created") {
                    val id = response.body()?.id
                    if (id!!.isNotEmpty()) {
                        teamList.add(Member(id, userId))
                    }
                    retrofitMakeMember(teamList,id,userId)
                }
            }
            override fun onFailure(call: Call<Team>, t: Throwable) {
                Log.d("실패","$t")
            }
        })
    }

    private fun retrofitMakeMember(teamList: MutableList<Member>, id: String, userId: String) {
        supplementService.makeMember(teamList)
            .enqueue(object : Callback<Map<String, Boolean>> {
                override fun onResponse(call: Call<Map<String, Boolean>>, response: Response<Map<String, Boolean>>) {
                    if (response.message() == "OK") {
                        getSharedPreferences("groupContext",Activity.MODE_PRIVATE).edit().clear().apply()
                        val teamList = getGroupPref(this@CertificationActivity, "groupContext")
                        val userList: MutableList<String> = ArrayList()
                        userList.add(userId)
                        val obj = TeamRoom(id = id!!, teamName = auto.getString("inputName","")!!, data = userList)
                        teamList.add(obj)
                        setGroupPref(this@CertificationActivity, "groupContext", teamList)
                        val intent = Intent(this@CertificationActivity,MainActivity::class.java)
                        startActivity(intent)
                        this@CertificationActivity.finish()
                    }
                }
                override fun onFailure(call: Call<Map<String, Boolean>>, t: Throwable) {
                }
            })
    }


}