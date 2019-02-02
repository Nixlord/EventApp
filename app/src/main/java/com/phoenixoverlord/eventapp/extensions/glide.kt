package com.phoenixoverlord.eventapp.extensions

import android.net.Uri
import android.os.Handler
import android.support.v4.content.FileProvider
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.phoenixoverlord.eventapp.base.BaseActivity
import com.phoenixoverlord.eventapp.extensions.Firebase.storage
import java.io.File
import java.lang.Error

fun BaseActivity.loadImage(imageView: ImageView, imageName : String) : ImageView {
    val storageReference = storage.child("images").child(imageName)
    return Glide.with(this)
        .load(storageReference)
        .apply(RequestOptions.centerCropTransform())
        .into(imageView)
        .view
}

fun BaseActivity.loadCircularImage(imageView: ImageView, imageName : String) : ImageView {
    val storageReference = storage.child("images").child(imageName)
    return Glide.with(this)
        .load(storageReference)
        .apply(RequestOptions.circleCropTransform())
        .into(imageView)
        .view
}


fun BaseActivity.loadImage(imageView: ImageView, imageFile : File) : ImageView {
    return Glide.with(this).load(imageFile).into(imageView).view
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
        "com.phoenixoverlord.fileprovider",
        file
    )
}