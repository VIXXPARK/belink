package com.capstone.belink.Model

data class User(val id:String,
                val phNum:String,
                val username:String,
                val active:Boolean=true,
                val admin:Boolean=false
)