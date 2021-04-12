package com.capstone.belink.UIActivity

import android.app.Activity
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.capstone.belink.Model.Success
import com.capstone.belink.Model.User
import com.capstone.belink.Network.RetrofitClient
import com.capstone.belink.Network.RetrofitService
import com.capstone.belink.R
import com.capstone.belink.databinding.ActivityEditInfoBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class EditInfoActivity : AppCompatActivity() {
    private var mBinding:ActivityEditInfoBinding?=null
    private val binding get() = mBinding!!

    private lateinit var pref: SharedPreferences
    private lateinit var edit: SharedPreferences.Editor

    private lateinit var retrofit : Retrofit
    private lateinit var supplementService : RetrofitService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding= ActivityEditInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pref = getSharedPreferences("auto",Activity.MODE_PRIVATE)
        edit= pref.edit()
        initRetrofit()
        binding.etEditName.setText(pref.getString("inputName","홍길동"))
        binding.etEditPhone.setText(pref.getString("inputPhone","01012345678"))
        binding.btnEditSend.setOnClickListener {
            val phoneNumber = binding.etEditPhone.text.toString()
            val name = binding.etEditName.text.toString()
            val id = pref.getString("userId","")!!
            connectUpdateInfo(phoneNumber,name,id)
        }
    }
    private fun initRetrofit() {
        retrofit = RetrofitClient.getInstance()
        supplementService = retrofit.create(RetrofitService::class.java)
    }

    private fun connectUpdateInfo(phoneNumber: String, name: String,id:String) {
        val user = User(id,phoneNumber,name)
        supplementService.editUser(user).enqueue(object : Callback<Success> {
            override fun onResponse(call: Call<Success>, response: Response<Success>) {
                Log.d("success",response.body().toString())
                setResult(Activity.RESULT_OK)
                finish()
            }

            override fun onFailure(call: Call<Success>, t: Throwable) {
                Log.d("fail","$t")
            }

        })

    }

    override fun onDestroy() {
        mBinding=null
        super.onDestroy()
    }
}