package com.capstone.belink.Utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.capstone.belink.Model.SearchLocation
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
            }
        }catch (e: JSONException){
            e.printStackTrace()
        }
    }
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

fun setSearchPref(context: Context,key: String,values:MutableList<SearchLocation>){
    val pref: SharedPreferences =context.getSharedPreferences(key, AppCompatActivity.MODE_PRIVATE)
    val edit: SharedPreferences.Editor = pref.edit()
    edit.putString(key,null)
    val dataList=JSONArray()
    for(i in values.indices){
        val tempJSONObject = JSONObject()
        tempJSONObject.put("address_name", values[i].address_name)
        tempJSONObject.put("category_group_code", values[i].category_group_code)
        tempJSONObject.put("category_group_name", values[i].category_group_name)
        tempJSONObject.put("category_name", values[i].category_name)
        tempJSONObject.put("distance", values[i].distance)
        tempJSONObject.put("id", values[i].id)
        tempJSONObject.put("phone", values[i].phone)
        tempJSONObject.put("place_name", values[i].place_name)
        tempJSONObject.put("place_url", values[i].place_url)
        tempJSONObject.put("road_address_name", values[i].road_address_name)
        tempJSONObject.put("x", values[i].x)
        tempJSONObject.put("y", values[i].y)
        dataList.put(tempJSONObject)
    }
    if(values.isNotEmpty()) {
        edit.putString(key, dataList.toString())
    }else{
        edit.putString(key,null)
    }
    edit.apply()
}
fun getSearchPref(context: Context,key: String):MutableList<SearchLocation>{
    val pref: SharedPreferences =context.getSharedPreferences(key, AppCompatActivity.MODE_PRIVATE)
    val json = pref.getString(key,null)
    val list: MutableList<SearchLocation> = ArrayList()
    if(json!=null){
        try{
            val temp = JSONArray(json)
            for(i in 0 until temp.length()){
                val iObject = temp.getJSONObject(i)
                val address_name = iObject.getString("address_name")
                val category_group_code = iObject.getString("category_group_code")
                val category_group_name = iObject.getString("category_group_name")
                val category_name = iObject.getString("category_name")
                val distance = iObject.getString("distance")
                val id = iObject.getString("id")
                val phone = iObject.getString("phone")
                val place_name = iObject.getString("place_name")
                val place_url = iObject.getString("place_url")
                val road_address_name = iObject.getString("road_address_name")
                val x = iObject.getString("x")
                val y = iObject.getString("y")
                val obj = SearchLocation(address_name = address_name, category_group_code = category_group_code, category_group_name = category_group_name,
                        category_name = category_name, distance = distance, id =id, phone = phone,
                        place_name =place_name, place_url = place_url, road_address_name = road_address_name, x = x, y = y)
                list.add(obj)
            }
        }catch (e:JSONException){
            e.printStackTrace()
        }
    }
    return list
}


