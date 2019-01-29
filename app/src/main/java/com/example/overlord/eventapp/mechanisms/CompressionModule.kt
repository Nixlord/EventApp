package com.example.overlord.eventapp.mechanisms

import android.os.Environment
import android.support.v7.app.AppCompatActivity
import com.example.overlord.eventapp.model.Constants
import id.zelory.compressor.Compressor
import java.io.File

fun AppCompatActivity.compressImage(image : File, name : String) : File {

    val destinationRoot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
    val destination = File(destinationRoot, Constants.localCompressedImages)

    // Do this in background
    val compressor = Compressor(this)
        .setDestinationDirectoryPath(destination.absolutePath)
        .setMaxWidth(1920)
        .setMaxHeight(1080)
        .setQuality(50)

    return compressor
        .compressToFile(image, name)
}