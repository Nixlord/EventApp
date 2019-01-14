package com.example.overlord.eventapp.model

import java.sql.Time
import java.util.*

class Event (
    val id : String = "",
    val name : String = "",
    val date : Date,
    val time : Time,
    val location : String = "",
    val image : String = "",
    val message : String? = ""
)