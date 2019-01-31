@file:JvmName("FragmentUtils")
package com.phoenixoverlord.eventapp.extensions

import android.support.v4.app.Fragment

fun Fragment.getSimpleName() : String {
    return this.javaClass.simpleName
}
