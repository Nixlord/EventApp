package com.example.overlord.eventapp.extensions

import com.example.overlord.eventapp.model.Constants
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.File
import java.io.FileInputStream

fun StorageReference.saveImage(compressedImage : File) : UploadTask {
    val imagesRef = FirebaseStorage.getInstance().reference.child(Constants.remoteCompressedImages)
    return imagesRef.child(timeStamp())
        .putStream(
            FileInputStream(
                compressedImage
            )
        )
}