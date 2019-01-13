package com.example.overlord.eventapp.model

import android.media.Image
import java.net.URL

// Hello Kotlin, Bye (almost) boilerplate.
class User(
        var name : String = "",
        var phoneno : String = "",
        var wedding_side : String = "Bride",
        var relation : String = "Friend",
        var key_contact : Boolean = false,
        var profile_photo : String = "")

val relationships = arrayListOf(
        "Bride", "Groom",
        "Mother", "Father", "Brother", "Sister",
        "Uncle", "Aunt", "Cousin",
        "Friend",
        "Sister-in-law", "Brother-in-law",
        "Wedding Photographer"
)