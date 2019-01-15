package com.example.overlord.eventapp.model

import android.graphics.drawable.Drawable
import java.sql.Time
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

class Event (
    val name : String = "",
    val date : LocalDate,
    val eventTime : LocalTime,
    val foodTime : LocalTime,
    val location : String = "",
    val image : Int,
    val message : String? = ""
)

val events = ArrayList<Event>()


