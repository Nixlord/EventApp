package com.example.overlord.eventapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity

/**
 * Created by diksha on 16/12/18.
 */
class SplashActivity : AppCompatActivity() {

    val SPLASH_TIME_OUT = 5000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({ startApp() }, SPLASH_TIME_OUT)
    }

    fun startApp() {
        snackbar("Successful SplashActivity")
        startActivity(Intent(this, IntroActivity::class.java))
        finish()
    }
}