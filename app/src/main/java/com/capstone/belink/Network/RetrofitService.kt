package com.capstone.belink.Network

import com.capstone.belink.Model.*
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


    @PUT("api/user/edit-info")
    fun editUser(@Body user: User): Call<successDTO>

    @GET("api/user/edit-info/{id}")
    fun deleteUser(@Path("id")id:String):Call<successDTO>

    @FormUrlEncoded
    @POST("api/user/get-my-friend")
    fun getMyFriend(@Field("id")id:String,@Field("hidden")hidden:Boolean):Call<FriendListDTO>

    @FormUrlEncoded
    @POST("api/user/edit-team")
    fun makeTeam(@Field("teamName")teamName:String):Call<SignDTO>


}