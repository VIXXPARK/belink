package com.capstone.belink.Ui

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.capstone.belink.Network.RetrofitClient
import com.capstone.belink.Network.RetrofitService
import com.capstone.belink.Network.SessionManager
import com.capstone.belink.UIActivity.EditInfoActivity
import com.capstone.belink.UIActivity.FriendSettingActivity
import com.capstone.belink.UIActivity.LoginActivity
import com.capstone.belink.UIActivity.MainActivity
import com.capstone.belink.databinding.FragmentEtcetraBinding
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
    private lateinit var autoLogin:SharedPreferences.Editor

    private lateinit var sessionManager: SessionManager

    private val PERMISSIONS_REQUEST_READ_CONTACTS = 1


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = FragmentEtcetraBinding.inflate(inflater,container,false)
        val view = binding.root
        (activity as AppCompatActivity).supportActionBar?.title="설정"



        auto = (activity as MainActivity).getSharedPreferences("auto",Activity.MODE_PRIVATE)!!
        autoLogin=auto.edit()
        sessionManager = SessionManager(xContext)
        initRetrofit()
        textViewClickListen()
        return view

    }



    private fun textViewClickListen() {

        binding.tvEtcetraManageFriend.setOnClickListener {
            val intent = Intent(xContext,FriendSettingActivity::class.java)
            requestContactPermission()
            startActivity(intent)

        }


        binding.tvEtcetraEditInfo.setOnClickListener {
            val intent = Intent(xContext,EditInfoActivity::class.java)
            intent.putExtra("inputName",auto.getString("inputName",""))
            intent.putExtra("inputPhone",auto.getString("inputPhone",""))
            (activity as MainActivity).startActivityForResult(intent,1)

        }

        binding.tvEtcetraUserOut.setOnClickListener {
            var builder = AlertDialog.Builder(xContext)
            builder.setTitle("회원탈퇴")
            builder.setMessage("정말로 회원탈퇴 하시겠습니까?")

            var listener = DialogInterface.OnClickListener { dialog, which ->
                when(which){
                    DialogInterface.BUTTON_POSITIVE -> {
                        Log.d("signOut", "탈퇴")
                        val id = auto.getString("userId","")!!
                        supplementService.deleteUser().enqueue(object : Callback<Map<String,Boolean>>{
                            override fun onResponse(call: Call<Map<String,Boolean>>, response: Response<Map<String,Boolean>>) {
                                if(response.message()=="OK")
                                    logOut()
                            }

                            override fun onFailure(call: Call<Map<String,Boolean>>, t: Throwable) {
                                Log.d("fail","$t")
                            }

                        })
                    }
                    DialogInterface.BUTTON_NEGATIVE -> Log.d("signout", "철회")
                }
            }
            builder.setPositiveButton("확인",listener)
            builder.setNegativeButton("취소",listener)
            builder.show()
        }

        binding.tvEtcetraLogOut.setOnClickListener {
            autoLogin.remove("auto").commit()
            logOut()
            Toast.makeText(xContext,"Logout",Toast.LENGTH_SHORT).show()

        }


    }

    fun logOut(){
        val intent = Intent(xContext, LoginActivity::class.java)
        startActivity(intent)
        autoLogin.clear()
        autoLogin.apply()
    }

    private fun initRetrofit() {
        retrofit = RetrofitClient.getInstance(xContext)
        supplementService = retrofit.create(RetrofitService::class.java)
    }



    override fun onAttach(context: Context) {
        mContext=context
        super.onAttach(context)
    }

    override fun onDestroy() {
        mBinding=null
        super.onDestroy()
    }

    private fun getContacts() {
        Toast.makeText((activity as MainActivity), "Get contacts ....", Toast.LENGTH_LONG).show()
    }
    fun requestContactPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    (activity as MainActivity),
                    Manifest.permission.READ_CONTACTS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        (activity as MainActivity),
                        Manifest.permission.READ_CONTACTS
                    )
                ) {
                    val builder: AlertDialog.Builder = AlertDialog.Builder((activity as MainActivity))
                    builder.setTitle("Read Contacts permission")
                    builder.setPositiveButton(android.R.string.ok, null)
                    builder.setMessage("Please enable access to contacts.")
                    builder.setOnDismissListener(DialogInterface.OnDismissListener {
                        requestPermissions(
                            arrayOf(
                                Manifest.permission.READ_CONTACTS
                            ), PERMISSIONS_REQUEST_READ_CONTACTS
                        )
                    })
                    builder.show()
                } else {
                    ActivityCompat.requestPermissions(
                        (activity as MainActivity), arrayOf(Manifest.permission.READ_CONTACTS),
                        PERMISSIONS_REQUEST_READ_CONTACTS
                    )
                }
            } else {
                getContacts()
            }
        } else {
            getContacts()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSIONS_REQUEST_READ_CONTACTS -> {
                if (grantResults.isNotEmpty()
                    && grantResults[0] === PackageManager.PERMISSION_GRANTED
                ) {
                    getContacts()
                } else {
                    Toast.makeText(
                        (activity as MainActivity),
                        "You have disabled a contacts permission",
                        Toast.LENGTH_LONG
                    ).show()
                }
                return
            }
        }
    }
}



