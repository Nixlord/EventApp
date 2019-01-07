@file:JvmName("Utils")
package com.example.overlord.eventapp.utils

import java.text.SimpleDateFormat
import java.util.*

fun timeStamp() = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
