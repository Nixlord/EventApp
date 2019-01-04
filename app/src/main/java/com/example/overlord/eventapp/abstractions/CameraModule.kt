package com.example.overlord.eventapp.abstractions

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import com.example.overlord.eventapp.extensions.logDebug
import pl.aprilapps.easyphotopicker.EasyImage
import java.io.File
import java.lang.Exception


fun AppCompatActivity.createCameraResultHandler(
    onSuccess : (File) -> Unit,
    onError : (Error) -> Unit)
        = fun(requestCode : Int, resultCode : Int, data: Intent?) {

    EasyImage.handleActivityResult(requestCode, resultCode, data, this, object : EasyImage.Callbacks {

        override fun onImagesPicked(p0: MutableList<File>, p1: EasyImage.ImageSource?, p2: Int) {

            p0.forEach { logDebug("CameraModuleHandler", it.toString()) }

            if (p0.size != 0) {
                val imageFile = p0[0]
                onSuccess(imageFile)
            }
            else {
                onError(Error("NO IMAGE"))
            }
        }

        override fun onImagePickerError(p0: Exception?, p1: EasyImage.ImageSource?, p2: Int) {
            onError(Error(p0?.message))
        }

        override fun onCanceled(p0: EasyImage.ImageSource?, p1: Int) {
            onError(Error("RESULT CANCELLED"))
        }
    })
}

