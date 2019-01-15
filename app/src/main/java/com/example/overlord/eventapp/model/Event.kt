package com.example.overlord.eventapp.model

import java.util.*

class Event (
    val name : String = "",
    val date : Date,
    val eventTime : String,
    val foodTime : String,
    val location : String = "",
    val image : Int,
    val message : String? = ""
)


