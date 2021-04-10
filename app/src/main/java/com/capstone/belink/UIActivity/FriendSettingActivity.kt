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
import com.capstone.belink.Utils.getStringArrayPref
import com.capstone.belink.Utils.setStringArrayPref
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
            getStringArrayPref(this,"contact")
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

        setStringArrayPref(this,"contact",contactList)

        Toast.makeText(this, "연락처 정보를 가져왔습니다.", Toast.LENGTH_SHORT).show()
    }


    override fun onDestroy() {
        mBinding=null
        super.onDestroy()
    }




}