package com.capstone.belink.Network

import com.capstone.belink.Model.*
import retrofit2.Call
import retrofit2.http.*

interface RetrofitService {

    @FormUrlEncoded
    @POST("/api/user/signup")
    fun registerUser(
        @Field("phNum")phNum:String,@Field("username")username:String
    ): Call<Sign>

    @FormUrlEncoded
    @POST("/api/user/get-user")
    fun getuser(@Field("phNum")phNum:String):Call<Sign>


    @PUT("api/user/edit-info")
    fun editUser(@Body user: User): Call<Success>

    @GET("api/user/edit-info/{id}")
    fun deleteUser(@Path("id")id:String):Call<Success>

    @FormUrlEncoded
    @POST("api/user/get-my-friend")
    fun getMyFriend(@Field("id")id:String,@Field("hidden")hidden:Boolean):Call<FriendList>

    @FormUrlEncoded
    @POST("api/user/edit-team")
    fun makeTeam(@Field("teamName")teamName:String):Call<Team>

    @FormUrlEncoded
    @POST("api/user/contact-user")
    fun contactUser(@Field("phNum")phNum:List<String>):Call<ContactInfo>

    @FormUrlEncoded
    @POST("api/user/id-contact-user")
    fun idContactUser(@Field("id")id:List<String>):Call<ContactInfo>


    @POST("api/user/make-member")
    fun makeMember(@Body teamList:MutableList<Member>):Call<Success>



}