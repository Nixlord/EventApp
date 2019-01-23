@file:JvmName("ActivityUtils")
package com.example.overlord.eventapp.extensions

import android.content.Intent
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import es.dmoral.toasty.Toasty

fun AppCompatActivity.snackbar(message : String) {
    // android.R.id.content Points to the layout file
    Snackbar.make(this.findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show()
}

fun AppCompatActivity.getSimpleName() : String {
    return this.javaClass.simpleName
}


fun AppCompatActivity.getTag() : String {
    val length = this.getSimpleName().length
    val till = Math.min(length - 1, 20)
    return this.getSimpleName().substring(0..till)
}

fun AppCompatActivity.toastSuccess(message : String) {
    Toasty.success(this, message, Toast.LENGTH_SHORT, true).show()
}

fun AppCompatActivity.toastError(message : String) {
    Toasty.error(this, message, Toast.LENGTH_SHORT, true).show()
}

fun AppCompatActivity.finishAndStart(activity : Class<*>) {
    startActivity(Intent(this, activity))
    finish()
}

fun AppCompatActivity.loadFragment(containerID : Int, fragment : Fragment) {
    supportFragmentManager.beginTransaction()
        .replace(containerID, fragment)
        .commit()
}



