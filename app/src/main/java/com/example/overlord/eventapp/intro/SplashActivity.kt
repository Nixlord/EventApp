package com.example.overlord.eventapp.intro

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.example.overlord.eventapp.R
import com.example.overlord.eventapp.common.finishAndStart

import com.example.overlord.eventapp.main.MainActivity
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {

    val SPLASH_TIME_OUT = 3000L

    //ToDo init using application class

    val auth = FirebaseAuth.getInstance()

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
            SPLASH_TIME_OUT
        )
    }
}