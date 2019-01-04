package com.example.overlord.eventapp.base

import android.content.Intent
import android.util.Log
import com.example.overlord.eventapp.extensions.compressImage
import com.example.overlord.eventapp.extensions.logDebug
import com.example.overlord.eventapp.extensions.saveImage
import com.example.overlord.eventapp.extensions.timeStamp
import com.example.overlord.eventapp.main.MainActivity
import com.example.overlord.eventapp.main.camera.snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.activity_camera.*
import pl.aprilapps.easyphotopicker.EasyImage
import java.io.File
import java.io.FileInputStream
import java.lang.Exception

/**
 * Try to push all this garbage into its own file and then expose via an extension function ?
 * Inheritance has been used in a very ugly manner.
 * This must be cleaned up.
 */

abstract class BaseActivity : ResultListenerActivity() {

    val firebaseAuth = FirebaseAuth.getInstance()
    val firebaseStorage = FirebaseStorage.getInstance().reference
    val firestore = FirebaseFirestore.getInstance()

    private var cameraResultHandler : ((requestCode : Int, resultCode : Int, data : Intent?) -> Unit)? = null

    interface onSuccessStep {
        fun onSuccess(onSuccess: (File) -> Unit) : onErrorStep
    }
    interface onErrorStep {
        fun onError(onError : (Error) -> Unit) : finalStep
    }
    interface finalStep {
        fun build()
    }

    inner class StepBuilder : onSuccessStep, onErrorStep, finalStep {

        lateinit var onSuccess: (File) -> Unit
        lateinit var onError: (Error) -> Unit

        override fun onSuccess(onSuccess: (image : File) -> Unit) : onErrorStep {
            this.onSuccess = onSuccess
            return this
        }

        override fun onError(onError: (Error) -> Unit) : finalStep {
            this.onError = onError
            return this
        }

        override fun build() {

            cameraResultHandler = fun(requestCode : Int, resultCode : Int, data: Intent?) {

                EasyImage.handleActivityResult(requestCode, resultCode, data, this@BaseActivity, object : EasyImage.Callbacks {

                    override fun onImagesPicked(p0: MutableList<File>, p1: EasyImage.ImageSource?, p2: Int) {

                        p0.forEach { logDebug("MainAct:Result", it.toString()) }

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
        }

    }

    fun takePhoto(chooserTitle : String) : onSuccessStep {

        EasyImage.openChooserWithDocuments(
            this,
            chooserTitle,
            1
        )

        return StepBuilder()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        cameraResultHandler?.invoke(requestCode, resultCode, data)
    }

    fun test1() {
        takePhoto("Please Select")
            .onSuccess { imageFile ->  }
    }
}















