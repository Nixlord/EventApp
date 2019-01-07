package com.example.overlord.eventapp.mechanisms

import android.graphics.Bitmap
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import com.example.overlord.eventapp.extensions.logDebug
import com.example.overlord.eventapp.extensions.logError
import com.example.overlord.eventapp.model.Constants
import com.example.overlord.eventapp.utils.timeStamp
import id.zelory.compressor.Compressor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File
/*
class CompressionModule {

    companion object {

        private val destinationRoot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        private val destination = File(destinationRoot, Constants.localCompressedImages)

        private lateinit var compressor : Compressor

        fun configureCompressor(activity : AppCompatActivity) {

        }
    }

    private lateinit var image : File

    private lateinit var onSuccess: ((File) -> Unit)
    private lateinit var onError: ((Error) -> Unit)

    private fun reset() {
        onSuccess = { image -> logDebug("DefaultCameraCallback", image.name) }
        onError = { error -> logError("DefaultCameraCallback", error.message) }
    }

    init { reset() }




    fun addOnSuccessListener(onSuccess: (File) -> Unit) {
        compressor.compressToFileAsFlowable(image, "IMG_${timeStamp()}")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                onSuccess,
                { throwable : Throwable ->
                    throwable.printStackTrace()
                    onError(Error(throwable.message))
                }
            )
            .dispose()
    }
}

fun AppCompatActivity.compressImage(image : File) : File {

    val destinationRoot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
    val destination = File(destinationRoot, Constants.localCompressedImages)

    // Do this in background
    val compressor = Compressor(this)
        .setDestinationDirectoryPath(destination.absolutePath)
        .setCompressFormat(Bitmap.CompressFormat.WEBP)
        .setMaxWidth(1920)
        .setMaxHeight(1080)
        .setQuality(75)

    return compressor
        .compressToBitmap()`
        .compressToFile(image, "IMG_${timeStamp()}")
}
*/