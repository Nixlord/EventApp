package com.example.overlord.eventapp.extensions

import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.crashlytics.android.Crashlytics
import java.lang.Exception


//Using Base Class Throwable instead of granular usage. Less resilient
fun AppCompatActivity.logError(error : Throwable) { logError(getTag(), error) }
fun logError(error : Throwable) { logError("GlobalLog", error) }
fun logError(tag : String = "GlobalLog", exception : Throwable) {

    exception.printStackTrace()

    val text = exception.message ?: "NullErrorMessage"

    Log.e(tag, text)
    Crashlytics.log(Log.ERROR, "F:$tag", text)
}


fun AppCompatActivity.logDebug(message: String?) { logDebug(getTag(), message) }
fun logDebug(tag : String = "GlobalLog", message: String?) {

    val text = message ?: "NullMessage"

    Log.d(tag, text)
    Crashlytics.log(Log.DEBUG, "F:$tag", text)
}
