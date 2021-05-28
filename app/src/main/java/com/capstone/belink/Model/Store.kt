package com.capstone.belink.Model

import java.util.*

data class Store(
        val id:String,
        val storeName:String,
        val storeLocation:String,
        val storeType:String,
        val companyNum:String,
        val token:String,
        val createdAt: Date,
        val updatedAt: Date,

)
