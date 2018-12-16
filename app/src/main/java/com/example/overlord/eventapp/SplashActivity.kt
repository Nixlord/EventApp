package com.example.overlord.eventapp

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    val SPLASH_TIME_OUT = 5000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({ startActivity(this, IntroActivity()) }, SPLASH_TIME_OUT)
    }
}