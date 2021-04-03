package com.capstone.belink

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.capstone.belink.Model.SignDTO
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

    private var phoneNum:String?=null
    private var name:String?=null

    private lateinit var retrofit : Retrofit
    private lateinit var supplementService : RetrofitService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRetrofit()

        var auto:SharedPreferences = getSharedPreferences("auto", Activity.MODE_PRIVATE)
        phoneNum=auto.getString("inputPhone",null)
        name=auto.getString("inputName",null)

        if(phoneNum !=null && name!=null){
            login(phoneNum!!)
        }
        else if(phoneNum==null && name==null){
            firstNum=binding.etPhoneFirst.text.toString()
            secondNum=binding.etPhoneSecond.text.toString()
            thirdNum=binding.etPhoneThird.text.toString()
            name=binding.etName.text.toString()
            phoneNum=firstNum+secondNum+thirdNum
            binding.btnSignupNext.setOnClickListener {
                var auto:SharedPreferences = getSharedPreferences("auto",Activity.MODE_PRIVATE)
                var autoLogin:SharedPreferences.Editor = auto.edit()
                autoLogin.putString("inputPhone",phoneNum)
                autoLogin.putString("inputName",name)
                signup(phoneNum!!,name!!)
                autoLogin.commit()

            }
            binding.btnLogin.setOnClickListener {
                var auto:SharedPreferences = getSharedPreferences("auto",Activity.MODE_PRIVATE)
                var autoLogin:SharedPreferences.Editor = auto.edit()
                autoLogin.putString("inputPhone",phoneNum)
                autoLogin.putString("inputName",name)
                login(phoneNum!!)
                autoLogin.commit()

            }
        }
    }

    private fun initRetrofit() {
        retrofit = RetrofitClient.getInstance()
        supplementService = retrofit.create(RetrofitService::class.java)
    }

    private fun signup(Phone: String, name: String) {


        supplementService.registerUser(Phone,name).enqueue(object : Callback<SignDTO>{
            override fun onResponse(call: Call<SignDTO>, response: Response<SignDTO>) {
                Log.d("success",response.message())
            }

            override fun onFailure(call: Call<SignDTO>, t: Throwable) {
                Log.d("fail","$t")

            }

        })
    }

    fun login(phoneNum: String) {

        supplementService.getuser(phoneNum).enqueue(object :Callback<SignDTO>{
            override fun onResponse(call: Call<SignDTO>, response: Response<SignDTO>) {
                if(response.message()=="OK"){
                    val intent = Intent(this@LoginActivity,MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            override fun onFailure(call: Call<SignDTO>, t: Throwable) {
            }
        })
    }
    override fun onDestroy() {
        mBinding=null
        super.onDestroy()
    }
}