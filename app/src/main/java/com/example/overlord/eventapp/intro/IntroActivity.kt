package com.example.overlord.eventapp.intro

import android.net.Uri
import android.os.Bundle
import android.widget.MediaController
import com.example.overlord.eventapp.R
import com.example.overlord.eventapp.base.BaseActivity
import com.example.overlord.eventapp.extensions.finishAndStart

import kotlinx.android.synthetic.main.activity_intro.*

class IntroActivity : BaseActivity() {

    var stopPosition : Int = 0

    /*

    Ex: lifecycle.addObserver(VideoPlayer(this))
    
    Todo Complete this later. Maybe use builder pattern to abstract all this 253dp
    Have tried BetterVideoPlayer.
    Its better not to use library here. Shit library.

    class VideoPlayer(activity: AppCompatActivity) : LifecycleObserver {

        val mediaController = MediaController(activity)
        var stopPosition : Int = 0

        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        fun onPause() {

        }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)


    }
 */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)


        // ToDo Move video to Cloud Storage and change code
        videoView.setVideoURI(Uri.parse("android.resource://" + packageName + "/" + R.raw.sns))
        videoView.setOnPreparedListener {
            val mediaController = MediaController(this)
            mediaController.setAnchorView(videoView)
            videoView.setMediaController(mediaController)
        }
        videoView.setOnCompletionListener { finishAndStart(LoginActivity::class.java) }
        videoView.start()
    }

    /* ToDo @Diksha, Why isFinishing? if isFinishing is there, nothing will happen?
    private fun jump() {
        if (isFinishing) return
        finishAndStart(LoginActivity::class.java)
    }
    */

    override fun onPause() {
        super.onPause()
        stopPosition = videoView.currentPosition //stopPosition is an int
        if (videoView.isPlaying)
            videoView.pause()
    }

    override fun onRestart() {
        super.onRestart()
        if (videoView != null) {
            videoView.seekTo(stopPosition)
            videoView.start()
        }
    }
}