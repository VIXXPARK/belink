package com.capstone.belink.Network

import com.capstone.belink.Model.Hello
import retrofit2.Call
import retrofit2.http.GET

interface RetrofitService {

    @GET("/")
    fun hello() : Call<Void>
}