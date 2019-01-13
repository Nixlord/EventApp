package com.example.overlord.eventapp.base

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import com.example.overlord.eventapp.mechanisms.ActivityResultHandler
import com.example.overlord.eventapp.mechanisms.CameraModule
import com.example.overlord.eventapp.mechanisms.PermissionsModule
import com.example.overlord.eventapp.utils.LoopingAtomicInteger
import icepick.Icepick
import io.reactivex.disposables.CompositeDisposable

abstract class BaseActivity : AppCompatActivity() {

    protected val compositeDisposable = CompositeDisposable()
    private val loopingAtomicInteger = LoopingAtomicInteger(100, 10000)
    private val activityResultHandler = ActivityResultHandler()
//    private val compressionModule = CompressionModule()
    private val permissionsModule = PermissionsModule()
    private val camera = CameraModule()

    /** CameraModule */
    fun takePhoto(prompt : String) = camera.takePhoto(this, prompt)

    /** ActivityResultModule */
    fun startActivityGetResult(
        intent : Intent,
        requestCode : Int = loopingAtomicInteger.nextInt()
    ) = activityResultHandler
        .createAction(requestCode)
        .perform {
            startActivityForResult(intent, requestCode)
        }

    /** PermissionsModule */
    fun withPermissions(vararg permissions : String) =
        permissionsModule.PermissionBuilder(loopingAtomicInteger.nextInt())
            .withPermissions(this, permissions.toCollection(ArrayList()))

    /** CompressionModule */
//    fun compressImage(image : File) = compressionModule.compressImage(this, image)


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        activityResultHandler.onActivityResult(requestCode, resultCode, data)
        camera.internalOnActivityResult(this, requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionsModule.onRequestPermissionsResult(requestCode, permissions, grantResults)
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

    override fun onStop() {
        super.onStop()
//        compressionModule.dispose()
    }
}