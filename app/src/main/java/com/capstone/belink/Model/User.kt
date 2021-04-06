package com.capstone.belink.Model

import java.util.*

data class User(val id:String,
                val phNum:String="01082828282",
                val username:String="belink",
                val active:Boolean=true,
                val admin:Boolean=false,
                val infect:Int=0,
                val createdAt: Date=Date(),
                val updatedAt: Date=Date()

                )