@file:JvmName("Utils")
package com.example.overlord.eventapp.extensions

import java.text.SimpleDateFormat
import java.util.*

fun timeStamp() = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())

