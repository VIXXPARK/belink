package com.capstone.belink.Model

import java.util.*

data class FriendDao (
        val hidden:Boolean,
        val updatedAt:Date,
        val deviceUser:FriendUserDao,
        val myFriendUser:FriendUserDao
        )