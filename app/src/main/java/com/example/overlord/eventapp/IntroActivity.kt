package com.example.overlord.eventapp

import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_intro.*

class IntroActivity :AppCompatActivity() {

    var stopPosition : Int = 0

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

    override fun onPause() {
        super.onPause()
        stopPosition = videoview.currentPosition //stopPosition is an int
        if (videoview.isPlaying)
            videoview.pause()
    }

    override fun onRestart() {
        super.onRestart()
        if (videoview != null) {
            videoview.seekTo(stopPosition)
            videoview.start()
        }
    }

}