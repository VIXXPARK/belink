package com.capstone.belink.UIActivity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.capstone.belink.Model.TeamRoom
import com.capstone.belink.Network.RetrofitClient
import com.capstone.belink.Network.RetrofitService
import com.capstone.belink.R
import com.capstone.belink.databinding.ActivityPopupBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class PopupActivity : AppCompatActivity() {

    private lateinit var retrofit: Retrofit
    private lateinit var supplementService: RetrofitService

    private var mBinding:ActivityPopupBinding?=null
    private val binding get() = mBinding!!

    private  lateinit var storeId: String
    private  lateinit var teamId: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding= ActivityPopupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRetrofit()
        storeId = intent.getStringExtra("FirebaseStoreId")
        teamId = intent.getStringExtra("FirebaseTeamId")
        binding.btnYes.setOnClickListener {
            retrofitNfcAccepted()
        }
        binding.btnNo.setOnClickListener {
            retrofitNfcRejected()
        }

    }
    private fun initRetrofit() {
        retrofit= RetrofitClient.getInstance(this)
        supplementService=retrofit.create(RetrofitService::class.java)
    }

    private fun retrofitNfcAccepted() {
        val userId = getSharedPreferences("auto", Activity.MODE_PRIVATE)!!.getString("userId","")!!
        println("team_room =$teamId ,storeId=$storeId,userId=$userId")
        // Toast.makeText(this@PopupActivity,"team_room =$teamId ,storeId=$storeId,userId=$userId",Toast.LENGTH_LONG).show()
        supplementService.nfcAccepted(team_room =teamId ,storeId=storeId,userId=userId).enqueue(object :
            Callback<Map<String, Boolean>> {
            override fun onResponse(call: Call<Map<String, Boolean>>, response: Response<Map<String, Boolean>>) {
                if(response.message()=="OK"){
                    val intent = Intent(this@PopupActivity,MainActivity::class.java)
                    startActivity(intent)
                }
            }
            override fun onFailure(call: Call<Map<String, Boolean>>, t: Throwable) {
            }
        })
    }

    private fun retrofitNfcRejected(){
        supplementService.nfcRejected(team_room = teamId).enqueue(object : Callback<Map<String,Boolean>>{
            override fun onResponse(
                call: Call<Map<String, Boolean>>,
                response: Response<Map<String, Boolean>>
            ) {
                if(response.message()=="OK"){
                    val intent = Intent(this@PopupActivity,MainActivity::class.java)
                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<Map<String, Boolean>>, t: Throwable) {
            }

        })
    }
}