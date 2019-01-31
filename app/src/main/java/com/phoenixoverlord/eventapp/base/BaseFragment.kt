package com.phoenixoverlord.eventapp.base

import android.support.v4.app.Fragment

abstract class BaseFragment : Fragment() {
    protected val base : BaseActivity by lazy { activity as BaseActivity }
}