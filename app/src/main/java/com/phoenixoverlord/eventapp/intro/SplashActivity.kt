package com.phoenixoverlord.eventapp.intro

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.phoenixoverlord.eventapp.R
import com.phoenixoverlord.eventapp.extensions.Firebase.auth
import com.phoenixoverlord.eventapp.extensions.finishAndStart

import com.phoenixoverlord.eventapp.main.MainActivity

class SplashActivity : AppCompatActivity() {

    private val SPLASH_TIME_OUT = 3000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        /*
        ToDo @Diksha
        Do not create activity objects for simple tasks like this, they are very heavy
        Handler().postDelayed(
            { startActivity(this, IntroActivity() },
            SPLASH_TIME_OUT
        )
        */
        Handler().postDelayed({
            finishAndStart(
                if (auth.currentUser != null)
                    MainActivity::class.java
                else
                    IntroActivity::class.java
            )},
            0
        )
    }
}