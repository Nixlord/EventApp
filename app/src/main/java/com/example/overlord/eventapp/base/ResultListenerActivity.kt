package com.example.overlord.eventapp.base

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import com.example.overlord.eventapp.abstractions.ActivityResultHandler
import icepick.Icepick
import java.util.concurrent.atomic.AtomicInteger

abstract class ResultListenerActivity : AppCompatActivity() {

    /** Activity Result Listener Implementation */
    private val activityResultHandler = ActivityResultHandler()

    //Todo Improve architecture
    fun startActivityGetResult(
        intent : Intent,
        requestCode : Int = activityResultHandler.nextRequestCode()
    ) = activityResultHandler
        .createAction(requestCode)
        .perform {
            startActivityForResult(intent, requestCode)
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        activityResultHandler.onActivityResult(requestCode, resultCode, data)
    }

    /** Stateful Portion */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Icepick.restoreInstanceState(this, savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        super.onSaveInstanceState(outState, outPersistentState)
        Icepick.saveInstanceState(this, outState)
    }
}