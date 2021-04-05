package com.capstone.belink.Ui

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.belink.Interface.IOnBackPressed
import com.capstone.belink.LoginActivity
import com.capstone.belink.MainActivity
import com.capstone.belink.Model.User
import com.capstone.belink.Model.successDTO
import com.capstone.belink.Network.RetrofitClient
import com.capstone.belink.Network.RetrofitService
import com.capstone.belink.databinding.FragmentEtcetraBinding
import com.capstone.belink.databinding.FragmentMapBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class FragmentEtcetra:Fragment() {
    private var mBinding:FragmentEtcetraBinding?=null
    private val binding get() = mBinding!!

    private var mContext:Context?=null
    private val xContext get() = mContext!!

    private lateinit var retrofit : Retrofit
    private lateinit var supplementService : RetrofitService

    private lateinit var auto: SharedPreferences
    private lateinit var autoLogin: SharedPreferences.Editor

    private var checkView:Int=0

    private lateinit var callback: OnBackPressedCallback

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = FragmentEtcetraBinding.inflate(inflater,container,false)
        val view = binding.root
        (activity as AppCompatActivity).supportActionBar?.title="설정"

        auto =(activity as MainActivity).getSharedPreferences("auto",Activity.MODE_PRIVATE)
        autoLogin=auto.edit()

        binding.etFragEditName.setText(auto.getString("inputName","홍길동"))
        binding.etFragEditPhone.setText(auto.getString("inputPhone","01012345678"))

        initRetrofit()


        binding.tvEtcetraEditInfo.setOnClickListener {
            if(binding.fragEtcetra.isVisible){
                binding.fragEtcetra.isVisible=false
                binding.fragEditView.isVisible=true
                checkView=1
            }
        }

        binding.tvEtcetraUserOut.setOnClickListener {
            var builder = AlertDialog.Builder(xContext)
            builder.setTitle("회원탈퇴")
            builder.setMessage("정말로 회원탈퇴 하시겠습니까?")

            var listener = object : DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    when(which){
                        DialogInterface.BUTTON_POSITIVE -> Log.d("signout","탈퇴")
                        DialogInterface.BUTTON_NEGATIVE -> Log.d("signout","철회")
                    }
                }

            }
            builder.setPositiveButton("확인",listener)
            builder.setNegativeButton("취소",listener)
            builder.show()
        }


        binding.btnFragEditSend.setOnClickListener {
            val phoneNumber = binding.etFragEditPhone.text.toString()
            val name = binding.etFragEditName.text.toString()
            val id = auto.getString("userId","")!!
            connectUpdateInfo(phoneNumber,name,id)
        }

        return view
    }


    private fun connectUpdateInfo(phoneNumber: String, name: String,id:String) {
        val user = User(id,phoneNumber,name)
        supplementService.editUser(user).enqueue(object : Callback<successDTO> {
            override fun onResponse(call: Call<successDTO>, response: Response<successDTO>) {
                Log.d("success",response.body().toString())
            }

            override fun onFailure(call: Call<successDTO>, t: Throwable) {
                Log.d("fail","$t")
            }

        })

    }

    override fun onDetach() {
        super.onDetach()
    }

    private fun initRetrofit() {
        retrofit = RetrofitClient.getInstance()
        supplementService = retrofit.create(RetrofitService::class.java)
    }



    override fun onAttach(context: Context) {
        mContext=context
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                if(checkView==1){
                    binding.fragEtcetra.isVisible=true
                    binding.fragEditView.isVisible=false
                    checkView=0
                }else{
                    isEnabled = false
                    (activity as MainActivity).onBackPressed()
                }

            }
        }

        (activity as MainActivity).onBackPressedDispatcher.addCallback(this,callback)


    }

    override fun onDestroy() {
        mBinding=null
        super.onDestroy()
        callback.remove()
    }


}



//binding.btnLogout.setOnClickListener {
//    val intent = Intent(xContext,LoginActivity::class.java)
//    startActivity(intent)
//    val auto = (activity as MainActivity).getSharedPreferences("auto",Activity.MODE_PRIVATE)
//    val editor = auto.edit()
//    editor.clear()
//    editor.apply()
//    Toast.makeText(xContext,"Logout",Toast.LENGTH_SHORT).show()
//
//}