package com.capstone.belink.Utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.capstone.belink.Model.TeamRoom
import com.capstone.belink.Model.User
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

fun setStringArrayPref(context: Context,key:String,values:MutableList<User>){ //연락처 정보 저장
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


fun getStringArraySaved(context: Context,key: String):MutableList<User>{ //연락처를 MutableList로 가져오기
    val pref : SharedPreferences = context.getSharedPreferences(key,AppCompatActivity.MODE_PRIVATE)
    val json = pref.getString(key,null)
    val list: MutableList<User> = ArrayList()
    if(json!=null){
        try{
            val temp = JSONArray(json)
            for(i in 0 until temp.length()){
                val iObject = temp.getJSONObject(i)
                val id = iObject.getString("id")
                val username = iObject.getString("username")
                val phNum = iObject.getString("phNum")
                val obj = User(id=id, phNum=phNum, username=username)
                list.add(obj)
            }
        }catch (e:JSONException){
            e.printStackTrace()
        }
    }
    return list
}


fun getStringArrayPref(context: Context,key:String):HashMap<String,String>{ //연락처를 HashMap으로 가져오기
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
                uri[phNum]=username
                Log.d(phNum, username)
            }
        }catch (e: JSONException){
            e.printStackTrace()
        }
    }
    Log.d("uri 값 확인",uri.toString())
    return uri
}

fun getMemberPref(context: Context,key: String):HashMap<String,Boolean>{ // 그룹에 관한 연락처 가져오기
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

fun setMemberPref(context: Context,key:String,values:HashMap<String,Boolean>){ // 그룹에 관한 연락처 저장하기
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

fun setGroupPref(context: Context,key:String,values:MutableList<TeamRoom>){
    val pref: SharedPreferences =context.getSharedPreferences(key, AppCompatActivity.MODE_PRIVATE)
    val edit: SharedPreferences.Editor = pref.edit()
    edit.putString(key,null)
    val dataList=JSONArray()
    for(i in values.indices){
        val tempJSONObject = JSONObject()
        tempJSONObject.put("id", values[i].id)
        tempJSONObject.put("teamName", values[i].teamName)
        val jsonArr1 = JSONArray()
        for(j in values[i].data.indices) {
            val subJSONObject = JSONObject()
            subJSONObject.put("id", (values[i].data)[j])
            jsonArr1.put(subJSONObject)
        }
       tempJSONObject.put("data",jsonArr1.toString())
        dataList.put(tempJSONObject)
    }
    println("setGroupPref--------")
    println(dataList.toString())
    println("--------------------")
    if(values.isNotEmpty()) {
        edit.putString(key, dataList.toString())
    }else{
        edit.putString(key,null)
    }
    edit.apply()
}

fun getGroupPref(context: Context,key: String):MutableList<TeamRoom>{
    val pref: SharedPreferences =context.getSharedPreferences(key, AppCompatActivity.MODE_PRIVATE)
    val json = pref.getString(key,null)
    val list: MutableList<TeamRoom> = ArrayList()

    if(json!=null){
        try{
            val temp = JSONArray(json)
            for(i in 0 until temp.length()){
                val iObject = temp.getJSONObject(i)
                val id = iObject.getString("id")
                val teamName = iObject.getString("teamName")
                val data = iObject.getString("data")
                val jsonArr2 = JSONArray(data)
                val friend:MutableList<String> = ArrayList()
                for(j in 0 until jsonArr2.length()){
                    val subObject = jsonArr2.getJSONObject(j)
                    val id = subObject.getString("id")
                    println("$j 번째 id: $id")
                    friend.add(id)
                }
                val obj = TeamRoom(id =id, teamName =teamName, data =friend)
                list.add(obj)
            }
        }catch (e:JSONException){
            e.printStackTrace()
        }

    }
    return list
}



