package com.phoenixoverlord.eventapp.model

import android.support.annotation.DrawableRes
import java.util.*

class Event (
    val name : String = "",
    val date : Date,
    val eventTime : String,
    val foodTime : String,
    val location : String = "",
    @DrawableRes val image : Int,
    val message : String? = ""
)


