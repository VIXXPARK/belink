package com.capstone.belink.Model

data class LoginResponse(
        val success:Boolean,
        val accessToken:String,
        val id:String
)
