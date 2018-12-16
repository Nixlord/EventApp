package com.example.overlord.eventapp

import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_intro.*

class IntroActivity :AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        videoview.setVideoURI(Uri.parse("android.resource://" + packageName + "/" + R.raw.sns))
        videoview.setOnCompletionListener { jump() }
        videoview.start()
    }

    private fun jump() {
        if (isFinishing) return
        startActivity(this, Login())
    }

/*
    override fun onStop() {
        super.onStop()
        startActivity(this, Login())
    }*/

}