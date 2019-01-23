package com.example.overlord.eventapp.extensions

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.overlord.eventapp.base.BaseActivity
import java.io.File

fun BaseActivity.loadImage(imageView: ImageView, imageName : String) {
    val storageReference = Firebase.storage.child("images").child(imageName)
    Glide.with(this).load(storageReference).into(imageView)
}

fun BaseActivity.loadImage(imageView: ImageView, imageFile : File) {
    Glide.with(this).load(imageFile).into(imageView)
}
