package com.example.overlord.eventapp.extensions

import android.content.Intent
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity

fun AppCompatActivity.snackbar(message : String) {
    // android.R.id.content Points to the layout file
    Snackbar.make(this.findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show()
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

fun AppCompatActivity.getName() : String {
    return this.javaClass.simpleName
}