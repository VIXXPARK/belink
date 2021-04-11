package com.capstone.belink.Utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.capstone.belink.Model.FriendUser
import com.capstone.belink.Model.User
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

fun setStringArrayPref(context: Context,key:String,values:MutableList<FriendUser>){
    val pref: SharedPreferences =context.getSharedPreferences(key, AppCompatActivity.MODE_PRIVATE)
    val edit: SharedPreferences.Editor = pref.edit()
    if(key=="contact") {
        edit.putString(key, null)
    }
    val dataList= JSONArray()
    for(i in values.indices){
        val tempJsonObject = JSONObject()
        tempJsonObject.put("id",values[i].id)
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


fun getStringArraySaved(context: Context,key: String):MutableList<FriendUser>{
    val pref : SharedPreferences = context.getSharedPreferences(key,AppCompatActivity.MODE_PRIVATE)
    val json = pref.getString(key,null)
    val list: MutableList<FriendUser> = ArrayList()
    if(json!=null){
        try{
            val temp = JSONArray(json)
            for(i in 0 until temp.length()){
                val iObject = temp.getJSONObject(i)
                val id = iObject.getString("id")
                val username = iObject.getString("username")
                val phNum = iObject.getString("phNum")
                val obj = FriendUser(id=id, phNum=phNum, username=username)
                list.add(obj)
            }
        }catch (e:JSONException){
            e.printStackTrace()
        }
    }
    return list
}


fun getStringArrayPref(context: Context,key:String):HashMap<String,String>{
    val pref: SharedPreferences =context.getSharedPreferences(key, AppCompatActivity.MODE_PRIVATE)
    val json=pref.getString(key,null)
    var uri : HashMap<String,String> = HashMap()
    if(json != null){
        try{
            val temp = JSONArray(json)
            for(i in 0 until temp.length()){
                val iObject = temp.getJSONObject(i)
                val username = iObject.getString("username")
                val phNum = iObject.getString("phNum")
                val obj = User(username=username,phNum = phNum)
                uri[phNum]=username
                Log.d("$phNum","$username")
            }
        }catch (e: JSONException){
            e.printStackTrace()
        }
    }
    Log.d("uri 값 확인",uri.toString())
    return uri
}

fun getMemberPref(context: Context,key: String):HashMap<String,Boolean>{
    val pref : SharedPreferences = context.getSharedPreferences(key,AppCompatActivity.MODE_PRIVATE)
    val json = pref.getString(key,null)
    val list: HashMap<String,Boolean> = HashMap()
    if(json!=null){
        try{
            val temp = JSONArray(json)
            for(i in 0 until temp.length()){
                val iObject = temp.getJSONObject(i)
                val id = iObject.getString("id")
                val isSelected = iObject.getBoolean("isSelected")
                list[id]=isSelected
            }
        }catch (e:JSONException){
            e.printStackTrace()
        }
    }
    return list
}

fun setMemberPref(context: Context,key:String,values:HashMap<String,Boolean>){
    val pref: SharedPreferences =context.getSharedPreferences(key, AppCompatActivity.MODE_PRIVATE)
    val edit: SharedPreferences.Editor = pref.edit()
    edit.putString(key, null)
    pref.edit().clear().apply()
    val dataList= JSONArray()
    for((k,v) in values){
        val tempJsonObject = JSONObject()
        tempJsonObject.put("id",k)
        tempJsonObject.put("isSelected",v)
        dataList.put(tempJsonObject)
    }
    if(values.isNotEmpty()) {
        edit.putString(key, dataList.toString())
    }else{
        edit.putString(key,null)
    }
    edit.apply()
}
