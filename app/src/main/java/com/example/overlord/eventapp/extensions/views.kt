@file:JvmName("ViewUtils")
package com.example.overlord.eventapp.extensions

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

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