package com.phoenixoverlord.eventapp.utils

import java.util.concurrent.atomic.AtomicInteger

class LoopingAtomicInteger(private val start : Int = 100, private val end : Int = 999) {
    private var atomicInteger = AtomicInteger(start)
    fun nextInt() : Int {
        atomicInteger.compareAndSet(end, start)
        return atomicInteger.incrementAndGet()
    }
}