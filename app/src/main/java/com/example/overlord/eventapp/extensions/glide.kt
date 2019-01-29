package com.example.overlord.eventapp.extensions

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.support.v4.content.FileProvider
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.overlord.eventapp.base.BaseActivity
import com.example.overlord.eventapp.extensions.Firebase.storage
import java.io.File
import java.lang.Error

fun BaseActivity.loadImage(imageView: ImageView, imageName : String) {
    val storageReference = storage.child("images").child(imageName)
    Glide.with(this).load(storageReference).into(imageView)
}

fun BaseActivity.loadImage(imageView: ImageView, imageFile : File) {
    Glide.with(this).load(imageFile).into(imageView)
}

fun BaseActivity.downloadImage(imageName: String, onSuccess : (file : File) -> Unit) {
    val storageReference = storage.child("images/$imageName")
    Glide.with(this)
        .asFile()
        .load(storageReference)
        .listener(object : RequestListener<File> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<File>?,
                isFirstResource: Boolean
            ): Boolean {
                logError(Error("LoadFailed"))
                return false
            }

            override fun onResourceReady(
                resource: File?,
                model: Any?,
                target: Target<File>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {

                if (resource != null)
                    onSuccess(resource)
                else
                    logError(Error("Resource Null"))

                return true
            }
        })
        .submit()
}

fun BaseActivity.getExternallyAccessibleURI(file : File) : Uri {
    return FileProvider.getUriForFile(
        this,
        "com.overlord.fileprovider",
        file
    )
}