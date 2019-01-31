@file:JvmName("Utils")
package com.phoenixoverlord.eventapp.utils

import java.text.SimpleDateFormat
import java.util.*

fun timeStamp() = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())

val loopingAtomicInteger = LoopingAtomicInteger(0, 10000)

fun uniqueName() = "${timeStamp()}_${loopingAtomicInteger.nextInt()}"