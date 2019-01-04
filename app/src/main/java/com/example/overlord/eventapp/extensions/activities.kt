@file:JvmName("ActivityUtils")
package com.example.overlord.eventapp.extensions

import android.content.Intent
import android.os.Environment
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.crashlytics.android.Crashlytics
import com.example.overlord.eventapp.abstractions.DexterStepBuilder
import com.example.overlord.eventapp.model.Constants
import id.zelory.compressor.Compressor
import java.io.File
import java.util.*



fun AppCompatActivity.snackbar(message : String) {
    // android.R.id.content Points to the layout file
    Snackbar.make(this.findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show()
}

fun AppCompatActivity.getName() : String {
    return this.javaClass.simpleName
}

fun logError(tag : String = "GlobalLog", message : String?) {

    val text = if (message != null) message else "NullMessage"

    Log.e(tag, text)
    Crashlytics.log(Log.ERROR, "F:$tag", text)
}

fun logDebug(tag : String = "GlobalLog", message: String?) {

    val text = if (message != null) message else "NullMessage"

    Log.d(tag, text)
    Crashlytics.log(Log.DEBUG, "F:$tag", text)
}

fun AppCompatActivity.getTag() : String {
    val length = this.getName().length
    val till = Math.min(length - 1, 20)
    return this.getName().substring(0..till)
}

fun AppCompatActivity.logError(message : String?) {
    logError(getTag(), message)
}

fun AppCompatActivity.logDebug(message: String?) {
    logDebug(getTag(), message)
}

fun AppCompatActivity.compressImage(image : File) : File {

    val destinationRoot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
    val destination = File(destinationRoot, Constants.localCompressedImages)

    return Compressor(this)
        .setDestinationDirectoryPath(destination.absolutePath)
        .compressToFile(image)
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

