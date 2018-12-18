package com.example.overlord.eventapp.common

import android.app.Activity
import android.content.Intent
import android.text.Editable
import android.widget.EditText
import android.text.TextWatcher
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*
import kotlin.reflect.KClass

fun EditText.onTextChange(onTextChange: (input : String) -> Unit ) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

        }

        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            onTextChange(charSequence.toString())
        }

        override fun afterTextChanged(editable: Editable) {

        }
    })
}


fun Activity.snackbar(message : String) {
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

// KClass is more idiomatic for Kotlin
fun Activity.finishAndStart(activity : Class<*>) {
    startActivity(Intent(this, activity))
    finish()
}

fun timeStamp() = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())

