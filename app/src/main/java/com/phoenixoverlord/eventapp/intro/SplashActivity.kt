package com.phoenixoverlord.eventapp.intro

import android.os.Bundle
import android.os.Handler
import com.phoenixoverlord.eventapp.R
import com.phoenixoverlord.eventapp.extensions.Firebase.auth
import com.phoenixoverlord.eventapp.extensions.finishAndStart
import com.phoenixoverlord.eventapp.main.MainActivity
import com.phoenixoverlord.eventapp.base.BaseActivity
import com.phoenixoverlord.eventapp.extensions.loadImage
import kotlinx.android.synthetic.main.activity_splash.*


class SplashActivity : BaseActivity() {

    private val SPLASH_TIME_OUT = 3000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        Handler().postDelayed({
            finishAndStart(
                if (auth.currentUser != null)
                    MainActivity::class.java
                else
                    IntroActivity::class.java
            )},
            SPLASH_TIME_OUT
        )
    }
}