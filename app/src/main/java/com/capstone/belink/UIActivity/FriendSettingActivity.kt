package com.capstone.belink.UIActivity

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.ContactsContract
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.capstone.belink.Model.ContactInfo
import com.capstone.belink.Model.FriendUser
import com.capstone.belink.Model.User
import com.capstone.belink.Network.RetrofitClient
import com.capstone.belink.Network.RetrofitService
import com.capstone.belink.Utils.getStringArrayPref
import com.capstone.belink.Utils.setStringArrayPref
import com.capstone.belink.databinding.ActivityFriendSettingBinding
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit


class FriendSettingActivity : AppCompatActivity() {

    private var mBinding:ActivityFriendSettingBinding?=null
    private val binding get() = mBinding!!



    private lateinit var retrofit: Retrofit
    private lateinit var supplementService: RetrofitService



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding= ActivityFriendSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRetrofit()

        binding.tvFriendReadContact.setOnClickListener {
            getContact()
            syncContact()

        }

    }



    private fun initRetrofit() {
        retrofit = RetrofitClient.getInstance()
        supplementService = retrofit.create(RetrofitService::class.java)
    }

    fun readContacts(view: View){
        getContact()
        syncContact()
    }

    private fun getContact(){ //주소 연락처 가져오기
        val contactList: MutableList<FriendUser> = ArrayList()
        val contacts = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        while (contacts!!.moveToNext()){
            val name = contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            val number = contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            val obj = FriendUser(id="",username = name, phNum = number)

            contactList.add(obj)
        }
        contacts.close()

        setStringArrayPref(this,"contact",contactList)//연락처를 preferences에 저장

        Toast.makeText(this, "연락처 정보를 가져왔습니다.", Toast.LENGTH_SHORT).show()
    }


    private fun syncContact(){ //주소 연락처에 있는 전화번호 중에 가입된 유저만 주소록 가져오기
        val contactUser=getStringArrayPref(this,"contact")
        println("contactUser.isEmpty() is ${contactUser.isEmpty()}")
        println("contactUser.keys is ${contactUser.keys}")
        val phNumList=contactUser.keys
        println("phNumList is $phNumList")
        val userList:MutableList<FriendUser> = ArrayList()
        supplementService.contactUser(phNumList.toList()).enqueue(object :Callback<ContactInfo>{
            override fun onResponse(call: Call<ContactInfo>, response: Response<ContactInfo>) {
                val data = response.body()?.data
                println("pass the response")
                println("${response.body()?.data}")
                if(data!=null){
                    for(i in data.indices){
                        println("pass the for loop")
                        val id = data[i].id
                        val username = data[i].username
                        val phNum = data[i].phNum
                        println("$id  $username   $phNum")
                        userList.add(FriendUser(id=id,username=username,phNum = phNum))
                    }
                }
                setStringArrayPref((this@FriendSettingActivity),"contact",userList) //연락처를 갱신

            }

            override fun onFailure(call: Call<ContactInfo>, t: Throwable) {
                Log.d("fail","$t")
            }

        })
    }


    override fun onDestroy() {
        mBinding=null
        super.onDestroy()
    }




}