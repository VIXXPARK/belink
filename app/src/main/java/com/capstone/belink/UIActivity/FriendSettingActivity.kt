package com.capstone.belink.UIActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Toast
import com.capstone.belink.Model.User
import com.capstone.belink.R
import com.capstone.belink.databinding.ActivityFriendSettingBinding

class FriendSettingActivity : AppCompatActivity() {
    private var mBinding:ActivityFriendSettingBinding?=null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding= ActivityFriendSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvFriendReadContact.setOnClickListener {
            val contactList: MutableList<User> = ArrayList()
            val contacts = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null)
            while (contacts!!.moveToNext()){
                val name = contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val number = contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                val obj = User(username = name,phNum = number)

                contactList.add(obj)
            }
            contacts.close()
            Toast.makeText(this,"연락처 정보를 가져왔습니다.",Toast.LENGTH_SHORT).show()
        }

    }

    override fun onDestroy() {
        mBinding=null
        super.onDestroy()
    }
}