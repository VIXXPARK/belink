package com.capstone.belink.UIActivity

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.capstone.belink.Model.*
import com.capstone.belink.Network.RetrofitClient
import com.capstone.belink.Network.RetrofitService
import com.capstone.belink.Network.SessionManager
import com.capstone.belink.Utils.setGroupPref
import com.capstone.belink.databinding.ActivityLoginBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.util.*
import kotlin.collections.ArrayList

/**
 * LoginActivity는 로그인 또는 회원가입에 관한 액티비티
 * 이때 해당 유저이름과 전화번호를 sharedPreferences에 key-value 형식으로 저장한다.
 * 그리고 로그인 버튼을 눌렀을 때 jwt토큰을 저장하기 위해 sessionManager를 호출하여
 * saveAuthToken메소드를 호출한다.
 * */
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


    private var TOKEN=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRetrofit()
        setFireMessageToken()
        setPhoneNumAndUsername()
        btnListener()

    }
    private fun initRetrofit() {
        retrofit = RetrofitClient.getInstance(this)
        supplementService = retrofit.create(RetrofitService::class.java)
    }

    private fun setFireMessageToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("태그", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result
            // Log and toast
            TOKEN = token.toString();

        })
    }

    private fun setPhoneNumAndUsername() {
        sessionManager = SessionManager(this)
        auto =getSharedPreferences("auto", Activity.MODE_PRIVATE)!!
        autoLogin=auto.edit()

        phoneNum=auto.getString("inputPhone", null)
        name=auto.getString("inputName", null)
    }

    private fun btnListener() {
        if(!name.isNullOrBlank() && !phoneNum.isNullOrBlank()){
            login(phoneNum!!)
        }
        binding.btnLoginSignup.setOnClickListener {
            getEditString()
            autoLogin.putString("token",TOKEN)
            autoLogin.apply()
            val intent = Intent(this,CertificationActivity::class.java)
            startActivity(intent)
        }
        binding.btnLoginLogin.setOnClickListener {
            getEditString()
            login(phoneNum!!)
        }
    }


    private fun getEditString(){
        getPhoneNumber()
        name=binding.etLoginName.text.toString()
        phoneNum= "$firstNum-$secondNum-$thirdNum"
        autoLogin.clear()
        autoLogin.putString("inputPhone", phoneNum)
        autoLogin.putString("inputName", name)
    }

    private fun getPhoneNumber() {
        firstNum=binding.etLoginPhoneFirst.text.toString()
        secondNum=binding.etLoginPhoneSecond.text.toString()
        thirdNum=binding.etLoginPhoneThird.text.toString()    }

    private fun login(phoneNum: String) {

        supplementService.login(phoneNum).enqueue(object : Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                val loginResponse = response.body()

                if (response.message() == "OK" && loginResponse?.accessToken != null) {
                    sessionManager.saveAuthToken(loginResponse!!.accessToken)
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    setSharedPreferencesEdit(response)
                    setGroupPreferencesEdit()
                    val teamMember = response.body()!!.id
                    supplementService.getMyTeam(teamMember).enqueue(object : Callback<GetMyTeam> {
                        override fun onResponse(
                            call: Call<GetMyTeam>,
                            response: Response<GetMyTeam>
                        ) {
                            if(response.message()=="OK"){
                                setGroupContentList(response)
                                startActivity(intent)
                                this@LoginActivity.finish()
                            }
                        }
                        override fun onFailure(call: Call<GetMyTeam>, t: Throwable) {
                            Log.d("태그","$t")
                        }
                    })
                }
            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
            }
        })
    }

    private fun setGroupPreferencesEdit(): SharedPreferences.Editor? {
        val groupEdit = getSharedPreferences("groupContext",Activity.MODE_PRIVATE)!!.edit()
        groupEdit.clear()
        groupEdit.apply()
        finish()
        return groupEdit
    }

    private fun setSharedPreferencesEdit(response: Response<LoginResponse>) {
        autoLogin.putString("userToken", response.body()!!.accessToken)
        autoLogin.putString("inputName", name)
        autoLogin.putString("inputPhone", phoneNum)
        autoLogin.putString("userId", response.body()!!.id)
        autoLogin.apply()
    }

    private fun setGroupContentList(response: Response<GetMyTeam>) {
        val teamList:ArrayList<TeamRoom> = ArrayList()
        response.body()!!.result.forEach{
            val element = TeamRoom(id =it.teamRoom.id, teamName = it.teamRoom.teamName,data = arrayListOf())
            teamList.add(element)
        }
        setGroupPref(this@LoginActivity,"groupContext",teamList)
    }

    override fun onDestroy() {
        mBinding=null
        super.onDestroy()
    }
}