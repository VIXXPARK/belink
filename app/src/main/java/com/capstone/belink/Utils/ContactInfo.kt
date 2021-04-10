package com.capstone.belink.Utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.capstone.belink.Model.User
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

fun setStringArrayPref(context: Context,key:String,values:MutableList<User>){
    val pref: SharedPreferences =context.getSharedPreferences(key, AppCompatActivity.MODE_PRIVATE)
    val edit: SharedPreferences.Editor = pref.edit()
    edit.putString(key,null)
    val dataList= JSONArray()
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

fun getStringArrayPref(context: Context,key:String):MutableList<User>{
    val pref: SharedPreferences =context.getSharedPreferences(key, AppCompatActivity.MODE_PRIVATE)
    val edit: SharedPreferences.Editor = pref.edit()
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
        }catch (e: JSONException){
            e.printStackTrace()
        }
    }
    return uri


}