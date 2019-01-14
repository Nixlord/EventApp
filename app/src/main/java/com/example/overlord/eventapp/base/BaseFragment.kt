package com.example.overlord.eventapp.base

import android.support.v4.app.Fragment

abstract class BaseFragment : Fragment() {
    protected val base = activity as BaseActivity
}