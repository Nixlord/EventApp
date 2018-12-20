package com.example.overlord.eventapp.main.common

import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.example.overlord.eventapp.Presenter
import com.example.overlord.eventapp.abstractions.OrchestratorInterface
import com.example.overlord.eventapp.extensions.getName

fun orchestratorFor(fragment: Fragment) : OrchestratorInterface {

    return when(fragment.getName()) {

        "WallFragment" -> /*Ex: `Wall*/Presenter
        "EventFragment" -> Presenter
        "AlbumFragment" -> Presenter
        "GuestsFragment" -> Presenter

        else -> throw IllegalArgumentException("Fragment '${fragment.getName()}' not found !")
    }
}

fun orchestratorFor(activity: AppCompatActivity) : OrchestratorInterface {

    return when(activity.getName()) {

        "MainActivity" -> Presenter
        "AboutActivity" -> Presenter

        else -> throw IllegalArgumentException("Activity '${activity.getName()}' not found !")
    }
}

