package com.capstone.belink.Network

import android.content.Context
import android.content.SharedPreferences
import com.capstone.belink.R

/*
* 토큰을 저장하고 저장한 것을 가져오기 위한 클래스*/
class SessionManager(context: Context) {
    private var pref: SharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name),Context.MODE_PRIVATE)

    companion object{
        const val USER_TOKEN = "user_token"
    }

    fun saveAuthToken(token:String){
        val editor = pref.edit()
        editor.putString(USER_TOKEN,token)
        editor.apply()
    }

    fun fetchAuthToken():String?{
        return pref.getString(USER_TOKEN,null)
    }
}