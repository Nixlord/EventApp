package com.example.overlord.eventapp

import android.app.Activity
import android.content.Intent
import android.text.Editable
import android.widget.EditText
import android.text.TextWatcher
import android.support.design.widget.Snackbar
import java.text.SimpleDateFormat
import java.util.*

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

fun Activity.startActivity(SourceActivity : Activity, FinalActivity : Activity) {
    startActivity(Intent(SourceActivity, FinalActivity::class.java))
    finish()
}

fun timeStamp() = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())

