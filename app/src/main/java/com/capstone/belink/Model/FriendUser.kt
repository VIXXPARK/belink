package com.capstone.belink.Model

data class FriendUser (
        val id:String,
        val username:String,
        val phNum:String,
        var isSelected:Boolean=false
        )