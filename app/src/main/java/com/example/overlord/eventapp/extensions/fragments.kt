@file:JvmName("FragmentUtils")
package com.example.overlord.eventapp.extensions

import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import es.dmoral.toasty.Toasty

fun Fragment.getSimpleName() : String {
    return this.javaClass.simpleName
}
