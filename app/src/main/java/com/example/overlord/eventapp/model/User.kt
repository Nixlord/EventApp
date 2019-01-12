package com.example.overlord.eventapp.model

import android.media.Image
import java.net.URL

// Hello Kotlin, Bye (almost) boilerplate.
class User(
        var name : String = "",
        var phoneno : String = "",
        var wedding_side : String = "",
        var relation : Relationship = Relationship.FRIEND,
        var key_contact : Boolean = false,
        var profile_photo : String = "")

enum class Relationship {
        BRIDE,
        GROOM,
        MOTHER, FATHER, BROTHER, SISTER,
        AUNT, UNCLE, COUSIN,
        SISTER_IN_LAW, BROTHER_IN_LAW,
        FRIEND,
        WEDDING_PHOTOGRAPHER
}