package com.capstone.belink.UIActivity

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.capstone.belink.Model.*
import com.capstone.belink.Network.RetrofitClient
import com.capstone.belink.Network.RetrofitService
import com.capstone.belink.Network.SessionManager
import com.capstone.belink.Utils.getGroupPref
import com.capstone.belink.Utils.setGroupPref
import com.capstone.belink.databinding.ActivityCertificationBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class CertificationActivity : AppCompatActivity() {
    private var mBinding: ActivityCertificationBinding? = null
    private val binding get() = mBinding!!

    private lateinit var retrofit: Retrofit
    private lateinit var supplementService: RetrofitService

    private lateinit var auto: SharedPreferences
    private lateinit var autoLogin: SharedPreferences.Editor

    private lateinit var phoneNum: String
    private lateinit var name: String

    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityCertificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSharedPreferences()
        sessionManager = SessionManager(this)
        initRetrofit()
        btnCertificationListener()
        btnSignListener()

    }

    private fun btnCertificationListener() {
        var phNum = auto.getString("inputPhone", "")!!
        val first = phNum.slice(IntRange(0, 2))
        val second = phNum.slice(IntRange(4, 7))
        val third = phNum.slice(IntRange(9, 12))
        phNum = first + second + third
        binding.btnSendNumber.setOnClickListener {
            supplementService.sendMsg(to = phNum).enqueue(object : Callback<Map<String, Boolean>> {
                override fun onResponse(
                    call: Call<Map<String, Boolean>>,
                    response: Response<Map<String, Boolean>>
                ) {
                    if (response.message() == "OK") {
                        Toast.makeText(this@CertificationActivity, "전송했습니다.", Toast.LENGTH_SHORT)
                            .show()
                        binding.btnCertCheck.isEnabled = true
                    }
                }

                override fun onFailure(call: Call<Map<String, Boolean>>, t: Throwable) {
                }
            })
        }

        binding.btnCertCheck.setOnClickListener {
            val content = binding.etCertification.text.toString()
            supplementService.sendCode(phNum = phNum, certNum = content)
                .enqueue(object : Callback<Map<String, Boolean>> {
                    override fun onResponse(
                        call: Call<Map<String, Boolean>>,
                        response: Response<Map<String, Boolean>>
                    ) {
                        if (response.message() == "OK") {
                            binding.btnCertification.isEnabled = true
                            binding.btnCertLogin.isEnabled = true
                        }
                    }

                    override fun onFailure(call: Call<Map<String, Boolean>>, t: Throwable) {
                    }
                })
        }
    }

    private fun setSharedPreferences() {
        auto = getSharedPreferences("auto", Activity.MODE_PRIVATE)!!
        autoLogin = auto.edit()
    }

    private fun initRetrofit() {
        retrofit = RetrofitClient.getInstance(this)
        supplementService = retrofit.create(RetrofitService::class.java)
    }

    private fun btnSignListener() {
        val TOKEN = auto.getString("token", "")!!
        phoneNum = auto.getString("inputPhone", "")!!
        name = auto.getString("inputName", "")!!
        binding.btnCertification.setOnClickListener {
            supplementService.registerUser(phoneNum, name, TOKEN).enqueue(object : Callback<Sign> {
                override fun onResponse(call: Call<Sign>, response: Response<Sign>) {
                    if (response.message() == "Created") {
                        val userId = response.body()!!.data.id
                        var teamList: MutableList<Member> = ArrayList()
                        retrofitMakeTeam(response, teamList, userId)
                    }
                }

                override fun onFailure(call: Call<Sign>, t: Throwable) {
                    Log.d("fail", "$t")
                }
            })
        }

        binding.btnCertLogin.setOnClickListener {
            val user = User(phNum = phoneNum, token = TOKEN)
            supplementService.refreshToken(user).enqueue(object : Callback<Map<String, Boolean>> {
                override fun onResponse(
                    call: Call<Map<String, Boolean>>,
                    response: Response<Map<String, Boolean>>
                ) {
                    login(phoneNum)
                }

                override fun onFailure(call: Call<Map<String, Boolean>>, t: Throwable) {
                    Log.d("fail", "$t")
                }

            })

        }
    }

    private fun retrofitMakeTeam(
        response: Response<Sign>,
        teamList: MutableList<Member>,
        userId: String
    ) {
        supplementService.makeTeam(response.body()!!.data.username)
            .enqueue(object : Callback<Team> {
                override fun onResponse(call: Call<Team>, response: Response<Team>) {
                    if (response.message() == "Created") {
                        val id = response.body()?.id
                        autoLogin.putString("teamRoomId", response.body()!!.id)
                        autoLogin.apply()
                        if (id!!.isNotEmpty()) {
                            teamList.add(Member(id, userId))
                        }
                        retrofitMakeMember(teamList, id, userId)
                    }
                }

                override fun onFailure(call: Call<Team>, t: Throwable) {
                    Log.d("실패", "$t")
                }
            })
    }

    private fun retrofitMakeMember(teamList: MutableList<Member>, id: String, userId: String) {
        supplementService.makeMember(teamList)
            .enqueue(object : Callback<Map<String, Boolean>> {
                override fun onResponse(
                    call: Call<Map<String, Boolean>>,
                    response: Response<Map<String, Boolean>>
                ) {
                    if (response.message() == "OK") {
                        getSharedPreferences("groupContext", Activity.MODE_PRIVATE).edit().clear()
                            .apply()
                        val teamList = getGroupPref(this@CertificationActivity, "groupContext")
                        val userList: MutableList<String> = ArrayList()
                        userList.add(userId)
                        val obj = TeamRoom(
                            id = id!!,
                            teamName = auto.getString("inputName", "")!!,
                            data = userList
                        )
                        teamList.add(obj)
                        setGroupPref(this@CertificationActivity, "groupContext", teamList)
                        login(phoneNum)
                    }
                }

                override fun onFailure(call: Call<Map<String, Boolean>>, t: Throwable) {
                }
            })
    }

    private fun login(phoneNum: String) {

        supplementService.login(phoneNum).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                val loginResponse = response.body()

                if (response.message() == "OK" && loginResponse?.accessToken != null) {
                    sessionManager.saveAuthToken(loginResponse!!.accessToken)
                    val intent = Intent(this@CertificationActivity, MainActivity::class.java)
                    setSharedPreferencesEdit(response)
                    setGroupPreferencesEdit()
                    val teamMember = response.body()!!.id
                    supplementService.getMyTeam(teamMember).enqueue(object : Callback<GetMyTeam> {
                        override fun onResponse(
                            call: Call<GetMyTeam>,
                            response: Response<GetMyTeam>
                        ) {
                            if (response.message() == "OK") {
                                setGroupContentList(response)
                                startActivity(intent)
                                this@CertificationActivity.finish()
                            }
                        }

                        override fun onFailure(call: Call<GetMyTeam>, t: Throwable) {
                            Log.d("태그", "$t")
                        }
                    })
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
            }
        })
    }

    private fun setGroupPreferencesEdit(): SharedPreferences.Editor? {
        val groupEdit = getSharedPreferences("groupContext", Activity.MODE_PRIVATE)!!.edit()
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
        val teamList: ArrayList<TeamRoom> = ArrayList()
        response.body()!!.result.forEach {
            val element =
                TeamRoom(id = it.teamRoom.id, teamName = it.teamRoom.teamName, data = arrayListOf())
            teamList.add(element)
        }
        setGroupPref(this@CertificationActivity, "groupContext", teamList)
    }
}