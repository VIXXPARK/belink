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
import com.capstone.belink.Model.User
import com.capstone.belink.databinding.ActivityFriendSettingBinding
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class FriendSettingActivity : AppCompatActivity() {
    private var mBinding:ActivityFriendSettingBinding?=null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding= ActivityFriendSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.tvFriendReadContact.setOnClickListener {
            getContact()
        }

        binding.btnGetContact.setOnClickListener {
            getStringArrayPref("contact")
        }
    }

    fun readContacts(view: View){
        getContact()
    }

    fun getContact(){
        val contactList: MutableList<User> = ArrayList()
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
            val obj = User(username = name, phNum = number)

            contactList.add(obj)
        }
        contacts.close()
        setStringArrayPref("contact",contactList)
        Toast.makeText(this, "연락처 정보를 가져왔습니다.", Toast.LENGTH_SHORT).show()
    }


    override fun onDestroy() {
        mBinding=null
        super.onDestroy()
    }

    fun setStringArrayPref(key:String,values:MutableList<User>){
        val pref:SharedPreferences=getSharedPreferences(key, MODE_PRIVATE)
        val edit:SharedPreferences.Editor = pref.edit()
        edit.putString(key,null)
        val dataList=JSONArray()
        for(i in values.indices){
            val tempJsonObject = JSONObject()
            Log.d("이름","${values[i].username}")
            Log.d("전화번호","${values[i].phNum}")
            tempJsonObject.put("username",values[i].username)
            tempJsonObject.put("phNum",values[i].phNum)
            dataList.put(tempJsonObject)
        }
        if(values.isNotEmpty()) {
            edit.putString(key, dataList.toString())
        }else{
            edit.putString(key,null)
        }
        edit.apply()
    }

    fun getStringArrayPref(key:String):MutableList<User>{
        val pref:SharedPreferences=getSharedPreferences(key, MODE_PRIVATE)
        val edit:SharedPreferences.Editor = pref.edit()
        val json=pref.getString(key,null)
        var uri : MutableList<User> = ArrayList()
        if(json != null){
            try{
                val temp = JSONArray(json)
                for(i in 0 until temp.length()){
                    val iObject = temp.getJSONObject(i)
                    val username = iObject.getString("username")
                    val phNum = iObject.getString("phNum")
                    val obj = User(username=username,phNum = phNum)
                    uri.add(obj)
                    Log.d("$phNum","$username")
                }
            }catch (e:JSONException){
                e.printStackTrace()
            }
        }
        return uri


    }




}