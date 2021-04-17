package com.capstone.belink.UIActivity

import android.app.ActionBar
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.capstone.belink.Model.Sign
import com.capstone.belink.Network.RetrofitClient
import com.capstone.belink.Network.RetrofitService
import com.capstone.belink.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class StartActivity : AppCompatActivity() {
    private lateinit var retrofit : Retrofit
    private lateinit var supplementService : RetrofitService

    private lateinit var auto: SharedPreferences
    private lateinit var autoLogin: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        initRetrofit()

        auto =getSharedPreferences("auto", Activity.MODE_PRIVATE)!!
        autoLogin=auto.edit()


        var actionBar = supportActionBar
        actionBar?.hide()

        val phoneNum = auto.getString("inputPhone",null)

        if(phoneNum.isNullOrBlank()){
            val intent = Intent(this@StartActivity,LoginActivity::class.java)
            startActivity(intent)
        }else{
            login(auto.getString("inputPhone","")!!)
        }

    }

    override fun onDestroy() {
        super.onDestroy()

    }

    private fun initRetrofit() {
        retrofit = RetrofitClient.getInstance()
        supplementService = retrofit.create(RetrofitService::class.java)
    }

    fun login(phoneNum: String) {
        supplementService.getuser(phoneNum).enqueue(object : Callback<Sign> {
            override fun onResponse(call: Call<Sign>, response: Response<Sign>) {
                println(response)
                if (response.message() == "OK") {
                    val intent = Intent(this@StartActivity, MainActivity::class.java)
                    autoLogin.putString("userId", response.body()?.data?.id.toString())
                    println(response.body())
                    println(response.body()?.data?.id.toString())
                    autoLogin.apply()
                    startActivity(intent)
                    finish()
                }else{
                    val intent = Intent(this@StartActivity,LoginActivity::class.java)
                    startActivity(intent)
                    this@StartActivity.finish()
                }
            }
            override fun onFailure(call: Call<Sign>, t: Throwable) {

            }
        })
    }
}