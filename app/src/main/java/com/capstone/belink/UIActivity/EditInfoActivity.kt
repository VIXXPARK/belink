package com.capstone.belink.UIActivity

import android.app.Activity
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.capstone.belink.Model.User
import com.capstone.belink.Network.RetrofitClient
import com.capstone.belink.Network.RetrofitService
import com.capstone.belink.Network.SessionManager
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

    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding= ActivityEditInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setPrefSharedPreferences()
        getEditContent()
        initRetrofit()
        setInfectionToggle()

    }

    private fun setInfectionToggle() {
        binding.swInfection.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                val id = pref.getString("userId","")!!
                val name = pref.getString("inputName","")!!
                supplementService.infectionPush(userId=id,name=name).enqueue(object : Callback<Map<String,Boolean>>{
                    override fun onResponse(
                        call: Call<Map<String, Boolean>>,
                        response: Response<Map<String, Boolean>>
                    ) {
                        if(response.message()=="OK"){
                            Toast.makeText(this@EditInfoActivity,"전송완료되었습니다.",Toast.LENGTH_SHORT).show()

                        }else{
                            Toast.makeText(this@EditInfoActivity,"실패했습니다.",Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onFailure(call: Call<Map<String, Boolean>>, t: Throwable) {
                    }
                })
            }
        }
    }

    private fun setPrefSharedPreferences() {
        pref = getSharedPreferences("auto",Activity.MODE_PRIVATE)
        edit= pref.edit()
        sessionManager = SessionManager(this)
    }

    private fun getEditContent() {
        binding.etEditName.setText(pref.getString("inputName","홍길동"))
        binding.btnEditSend.setOnClickListener {
            val phoneNumber = pref.getString("inputPhone","01012345678")!!
            val name = binding.etEditName.text.toString()
            val id = pref.getString("userId","")!!
            connectUpdateInfo(phoneNumber,name,id)
        }
    }

    private fun initRetrofit() {
        retrofit = RetrofitClient.getInstance(this)
        supplementService = retrofit.create(RetrofitService::class.java)
    }

    private fun connectUpdateInfo(phoneNumber: String, name: String,id:String) {
        val user = User(id,phoneNumber,name)
        supplementService.editUser(user).enqueue(object : Callback<Map<String,Boolean>> {
            override fun onResponse(call: Call<Map<String,Boolean>>, response: Response<Map<String,Boolean>>) {
                edit.putString("inputName",binding.etEditName.text.toString())
                edit.apply()
                setResult(Activity.RESULT_OK)
                finish()
            }
            override fun onFailure(call: Call<Map<String,Boolean>>, t: Throwable) {
                Log.d("fail","$t")
            }
        })
    }

    override fun onDestroy() {
        mBinding=null
        super.onDestroy()
    }
}
