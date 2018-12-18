package com.example.overlord.eventapp.intro

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.example.overlord.eventapp.R
import com.example.overlord.eventapp.common.startActivity

class SplashActivity : AppCompatActivity() {

    val SPLASH_TIME_OUT = 3000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed(
            { startActivity(this, IntroActivity()) },
            SPLASH_TIME_OUT
        )
    }
}