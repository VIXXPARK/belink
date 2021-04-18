package com.capstone.belink.UIActivity

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.capstone.belink.Model.LoginResponse
import com.capstone.belink.Model.Sign
import com.capstone.belink.Network.RetrofitClient
import com.capstone.belink.Network.RetrofitService
import com.capstone.belink.Network.SessionManager
import com.capstone.belink.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.util.*


class LoginActivity : AppCompatActivity() {
    private var mBinding:ActivityLoginBinding?=null
    private val binding get() = mBinding!!

    private var firstNum=""
    private var secondNum=""
    private var thirdNum=""

    private var phoneNum:String?=null
    private var name:String?=null

    private lateinit var retrofit : Retrofit
    private lateinit var supplementService : RetrofitService

    private lateinit var auto:SharedPreferences
    private lateinit var autoLogin:SharedPreferences.Editor

    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        initRetrofit()

        auto =getSharedPreferences("auto", Activity.MODE_PRIVATE)!!
        autoLogin=auto.edit()

        phoneNum=auto.getString("inputPhone", null)
        name=auto.getString("inputName", null)

        if(!name.isNullOrBlank() && !phoneNum.isNullOrBlank()){
            Log.d("status", "first_if")
            login(phoneNum!!)
        }

        binding.btnLoginSignup.setOnClickListener {
            getEditString()
            autoLogin.apply()
            signup(phoneNum!!, name!!)
        }
        binding.btnLoginLogin.setOnClickListener {
            getEditString()
            login(phoneNum!!)
        }

    }

    fun getEditString(){
        firstNum=binding.etLoginPhoneFirst.text.toString()
        secondNum=binding.etLoginPhoneSecond.text.toString()
        thirdNum=binding.etLoginPhoneThird.text.toString()
        name=binding.etLoginName.text.toString()
        phoneNum= "$firstNum-$secondNum-$thirdNum"
//        phoneNum =firstNum+secondNum+thirdNum
        autoLogin.clear()
        autoLogin.putString("inputPhone", phoneNum)
        autoLogin.putString("inputName", name)
    }

    private fun initRetrofit() {
        retrofit = RetrofitClient.getInstance(this)
        supplementService = retrofit.create(RetrofitService::class.java)
    }

    private fun signup(Phone: String, name: String) {

        supplementService.registerUser(Phone, name).enqueue(object : Callback<Sign> {
            override fun onResponse(call: Call<Sign>, response: Response<Sign>) {
                Log.d("Phone", Phone)
                Log.d("Name", name)
                Log.d("success", response.message())
            }

            override fun onFailure(call: Call<Sign>, t: Throwable) {
                Log.d("fail", "$t")
            }
        })
    }

    fun login(phoneNum: String) {

        supplementService.login(phoneNum).enqueue(object : Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                val loginResponse = response.body()

                if(response.message()=="OK" && loginResponse?.accessToken!=null){
                    sessionManager.saveAuthToken(loginResponse!!.accessToken)
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    autoLogin.putString("userToken", response.body()?.accessToken)
                    autoLogin.putString("inputName",name)
                    autoLogin.putString("inputPhone", phoneNum)
                    println(response.body())
                    println(response.body()?.accessToken)
                    autoLogin.apply()
                    startActivity(intent)
                    this@LoginActivity.finish()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {

            }

        })

    }
    override fun onDestroy() {
        mBinding=null
        super.onDestroy()
    }
}