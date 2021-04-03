package com.capstone.belink.Network

import com.capstone.belink.Model.FriendDao
import com.capstone.belink.Model.SignDao
import retrofit2.Call
import retrofit2.http.*

interface RetrofitService {

    @FormUrlEncoded
    @POST("/api/user/signup")
    fun registerUser(
        @Field("phNum")phNum:String,@Field("username")username:String
    ): Call<SignDao>

    @FormUrlEncoded
    @POST("/api/user/get-user")
    fun getuser(@Field("phNum")phNum:String):Call<SignDao>

    @FormUrlEncoded
    @POST("api/user/get-my-friend")
    fun getMyFriend(@Field("id")id:String):Call<List<FriendDao>>

}