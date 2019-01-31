package com.phoenixoverlord.eventapp.utils

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent


class SwipeDisabledViewPager(context: Context, attrs: AttributeSet) : ViewPager(context, attrs) {

    var shouldInterceptTouch: Boolean = false

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (shouldInterceptTouch) {
            super.onTouchEvent(event)
        } else false
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return if (shouldInterceptTouch) {
            super.onInterceptTouchEvent(event)
        } else false
    }
}