package com.example.overlord.eventapp.base

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import com.example.overlord.eventapp.mechanisms.ActivityResultHandler
import com.example.overlord.eventapp.mechanisms.CameraModule
import com.example.overlord.eventapp.utils.LoopingAtomicInteger
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import icepick.Icepick

abstract class BaseActivity : AppCompatActivity() {

    val firebaseAuth = FirebaseAuth.getInstance()
    val firebaseStorage = FirebaseStorage.getInstance().reference
    val firestore = FirebaseFirestore.getInstance() //Has reference to Context, can't be placed in singleton


    /** Activity Result Listener Implementation */
    private val loopingAtomicInteger = LoopingAtomicInteger(100, 999)
    private val activityResultHandler = ActivityResultHandler()
    private val camera = CameraModule()

    fun takePhoto(prompt : String) = camera.takePhoto(this, prompt)

    //Todo Improve architecture
    fun startActivityGetResult(
        intent : Intent,
        requestCode : Int = loopingAtomicInteger.nextInt()
    ) = activityResultHandler
        .createAction(requestCode)
        .perform {
            startActivityForResult(intent, requestCode)
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        activityResultHandler.onActivityResult(requestCode, resultCode, data)
        camera.internalOnActivityResult(this, requestCode, resultCode, data)
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