package com.phoenixoverlord.eventapp.mechanisms

import android.content.Intent
import com.phoenixoverlord.eventapp.base.BaseActivity
import com.phoenixoverlord.eventapp.extensions.logDebug
import com.phoenixoverlord.eventapp.extensions.logError
import pl.aprilapps.easyphotopicker.EasyImage
import java.io.File
import java.lang.Exception

// Not Thread Safe, May not be required. Requires Polish
class CameraModule {
    private lateinit var onSuccess: ((MutableList<File>) -> Unit)
    private lateinit var onError: ((Error) -> Unit)

    private fun reset() {
        onSuccess = { images -> images.forEach { image -> logDebug("DefaultCameraCallback", image.name) } }
        onError = { error -> logError("DefaultCameraCallback", error) }
    }

    init { reset() }

    fun takePhoto(activity : BaseActivity, prompt : String = "Upload Photo") : CameraModule {
        EasyImage.openChooserWithGallery(
            activity,
            prompt,
            1)
        return this
    }

    fun addOnSuccessListener(onSuccess: (MutableList<File>) -> Unit) : CameraModule{
        this.onSuccess = onSuccess
        return this
    }
    fun addOnFailureListener(onError: (Error) -> Unit): CameraModule {
        this.onError = onError
        return this
    }

    // ToDo Can't figure out how to hide this from global namespace
    fun internalOnActivityResult(
        activity : BaseActivity,
        requestCode : Int,
        resultCode : Int,
        data: Intent?
    ) {
        EasyImage.handleActivityResult(requestCode, resultCode, data, activity, object : EasyImage.Callbacks {

            override fun onImagesPicked(p0: MutableList<File>, p1: EasyImage.ImageSource?, p2: Int) {

                onSuccess(p0)
                p0.forEach { image ->
                    logDebug("CameraModuleHandler", image.toString())
                }

                reset()
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
