@file:JvmName("ActivityUtils")
package com.example.overlord.eventapp.extensions

import android.content.Intent
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.crashlytics.android.Crashlytics
import com.example.overlord.eventapp.abstractions.ActivityResultAction
import com.example.overlord.eventapp.abstractions.DexterStepBuilder
import pl.aprilapps.easyphotopicker.EasyImage
import java.util.*



fun AppCompatActivity.snackbar(message : String) {
    // android.R.id.content Points to the layout file
    Snackbar.make(this.findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show()
}

fun AppCompatActivity.getName() : String {
    return this.javaClass.simpleName
}




fun logError(tag : String = "GlobalLog", message : String) {
    Log.e(tag, message)
    Crashlytics.log(Log.ERROR, tag, message)
}

fun logDebug(tag : String = "GlobalLog", message: String) {
    Log.d(tag, message)
    Crashlytics.log(Log.DEBUG, tag, message)
}

fun AppCompatActivity.logError(message : String) {
    val tag = this.getName().substring(0..20)
    logError(tag, message)
}

fun AppCompatActivity.logDebug(message: String) {
    val tag = this.getName().substring(0..20)
    logDebug(tag, message)
}



/*
> No memory leak here, but be careful with activity references.
> Source activity is the this parameter, so you don't need to pass,
> Follow variable naming conventions
> Be careful while giving names similar to default methods.


fun Activity.startActivity(SourceActivity : Activity, FinalActivity : Activity) {
    startActivity(Intent(SourceActivity, FinalActivity::class.java))
    finish()
}
*/

// Activity lifecycle runs to completion after calling finish, so have to be careful
// Finish does not instantly destroy the activity
// Possible Concern -> Not like other startActivity functions
fun AppCompatActivity.finishAndStart(activity : Class<*>) {
    startActivity(Intent(this, activity))
    finish()
}

fun AppCompatActivity.loadFragment(containerID : Int, fragment : Fragment) {
    val currentFragment = supportFragmentManager.findFragmentById(containerID)

    if (currentFragment != null)
        supportFragmentManager.beginTransaction()
            .add(containerID, fragment)
            .commit()
    else
        supportFragmentManager.beginTransaction()
            .replace(containerID, fragment)
            .commit()
}

fun AppCompatActivity.withPermissions(vararg permissions : String?) =
    DexterStepBuilder.withActivity(this).requestPermissions(permissions.toCollection(ArrayList()))

fun AppCompatActivity.takePhoto() {
    val action = {
        EasyImage.openChooserWithDocuments(
            this,
            "Please Upload Photo",
            0
        )
    }

    val onSuccess : (Intent) -> Unit = {
            intent ->
                logDebug(intent.dataString)

    }

    val onError : (Error) -> Unit = {
        error ->
            logError(error.message!!)
    }

    val resultAction = ActivityResultAction(action, onSuccess, onError);
}