@file:JvmName("Utils")
package com.phoenixoverlord.eventapp.utils

import com.phoenixoverlord.eventapp.extensions.Firebase.auth
import java.text.SimpleDateFormat
import java.util.*

fun timeStamp() = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())

val loopingAtomicInteger = LoopingAtomicInteger(0, 10000)

fun uniqueName() = "${timeStamp()}_${auth.currentUser?.uid.hashCode()}_${loopingAtomicInteger.nextInt()}"