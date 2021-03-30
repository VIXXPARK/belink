package com.capstone.belink.Network

import retrofit2.Call
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET

interface RetrofitService {

    @FormUrlEncoded
    @GET("")
    fun hello() : Call<String>
}