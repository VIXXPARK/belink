package com.capstone.belink.Model

import java.util.*

data class FriendDTO (
        val hidden:Boolean,
        val updatedAt:Date,
        val deviceUser:FriendUserDTO,
        val myFriendUser:FriendUserDTO
        )