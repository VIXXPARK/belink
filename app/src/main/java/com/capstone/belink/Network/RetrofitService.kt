package com.capstone.belink.Network

import com.capstone.belink.Model.FriendListDTO
import com.capstone.belink.Model.SignDTO
import com.capstone.belink.Model.User
import com.capstone.belink.Model.successDTO
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface RetrofitService {

    @FormUrlEncoded
    @POST("/api/user/signup")
    fun registerUser(
        @Field("phNum")phNum:String,@Field("username")username:String
    ): Call<SignDTO>

    @FormUrlEncoded
    @POST("/api/user/get-user")
    fun getuser(@Field("phNum")phNum:String):Call<SignDTO>

    @FormUrlEncoded
    @POST("api/user/get-my-friend")
    fun getMyFriend(@Field("id")id:String):Call<FriendListDTO>

    @PUT("api/user/edit-info")
    fun editUser(@Body user: User): Call<successDTO>

}