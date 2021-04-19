package com.capstone.belink.UIActivity

import android.app.ActionBar
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.capstone.belink.Model.LoginResponse
import com.capstone.belink.Model.Sign
import com.capstone.belink.Network.RetrofitClient
import com.capstone.belink.Network.RetrofitService
import com.capstone.belink.Network.SessionManager
import com.capstone.belink.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
/**
 * StartActivity는 처음 화면이 나오는 액티비티로서
 * 로그인 유무를 판단하고 로그인이 되었을 때에는 바로 메인액티비티로 전환
 * 그렇지 않을 경우에는 로그인 페이지로 전달한다.
 * 로그인 확인은 jwt토큰의 유무로 판단*/
class StartActivity : AppCompatActivity() {
    private lateinit var retrofit : Retrofit
    private lateinit var supplementService : RetrofitService

    private lateinit var auto: SharedPreferences
    private lateinit var autoLogin: SharedPreferences.Editor

    private lateinit var sessionManager:SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        initRetrofit()

        auto =getSharedPreferences("auto", Activity.MODE_PRIVATE)!!
        autoLogin=auto.edit()
        sessionManager= SessionManager(this)


        /**
         * 액션바를 없앨 때에는 해당 액션바를 부른 supportActionbar를 호출하고 hide()메소드를 호출하면 끝!*/
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
        retrofit = RetrofitClient.getInstance(this)
        supplementService = retrofit.create(RetrofitService::class.java)
    }

    fun login(phoneNum: String) {
        supplementService.login(phoneNum).enqueue(object : Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                val loginResponse = response.body()
                if(response.message()=="OK" && loginResponse?.accessToken!=null){
                    sessionManager.saveAuthToken(loginResponse!!.accessToken)
                    val intent = Intent(this@StartActivity, MainActivity::class.java)
                    autoLogin.putString("userToken", response.body()?.accessToken)
                    println(response.body())
                    println(response.body()?.accessToken)
                    autoLogin.apply()
                    startActivity(intent)
                    this@StartActivity.finish()
                }else{
                    val intent = Intent(this@StartActivity,LoginActivity::class.java)
                    startActivity(intent)
                    this@StartActivity.finish()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {

            }

        })


    }
}