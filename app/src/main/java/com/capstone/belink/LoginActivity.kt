package com.capstone.belink

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.capstone.belink.Model.User
import com.capstone.belink.Network.RetrofitClient
import com.capstone.belink.Network.RetrofitService
import com.capstone.belink.databinding.ActivityLoginBinding
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class LoginActivity : AppCompatActivity() {
    private var mBinding:ActivityLoginBinding?=null
    private val binding get() = mBinding!!

    private var firstNum=""
    private var secondNum=""
    private var thirdNum=""
    private var name=""

    private lateinit var retrofit : Retrofit
    private lateinit var supplementService : RetrofitService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRetrofit()

        binding.btnSignupNext.setOnClickListener {
            firstNum=binding.etPhoneFirst.text.toString()
            secondNum=binding.etPhoneSecond.text.toString()
            thirdNum=binding.etPhoneThird.text.toString()
            name=binding.etName.text.toString()
            firstNum+=secondNum+thirdNum
            Toast.makeText(this,firstNum,Toast.LENGTH_SHORT).show()
            signup(firstNum,name)

        }
    }

    private fun initRetrofit() {
        retrofit = RetrofitClient.getInstance()
        supplementService = retrofit.create(RetrofitService::class.java)
    }

    private fun signup(Phone: String, name: String) {


        supplementService.registerUser(Phone,name).enqueue(object : Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                Log.d("success",response.message())
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d("fail","failure")

            }

        })
    }

    override fun onDestroy() {
        mBinding=null
        super.onDestroy()
    }
}