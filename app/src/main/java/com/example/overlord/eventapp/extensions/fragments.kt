@file:JvmName("FragmentUtils")
package com.example.overlord.eventapp.extensions

import android.support.v4.app.Fragment

fun Fragment.getName() : String {
    return this.javaClass.simpleName
}