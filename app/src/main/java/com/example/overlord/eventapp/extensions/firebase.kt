package com.example.overlord.eventapp.extensions

import com.example.overlord.eventapp.utils.LoopingAtomicInteger
import com.example.overlord.eventapp.model.Constants
import com.example.overlord.eventapp.utils.timeStamp
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.File
import java.io.FileInputStream

// Max 100 images can be sent at once
val loopingAtomicInteger = LoopingAtomicInteger(0, 100)

fun StorageReference.pushImage(compressedImage : File) : UploadTask {
    val imagesRef = FirebaseStorage.getInstance().reference.child(Constants.remoteCompressedImages)
    return imagesRef.child("${timeStamp()}_${loopingAtomicInteger.nextInt()}")
        .putStream(
            FileInputStream(
                compressedImage
            )
        )
}
    