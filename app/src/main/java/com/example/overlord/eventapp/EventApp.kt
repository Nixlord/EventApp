package com.example.overlord.eventapp

import android.app.Application
import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.example.overlord.eventapp.model.Constants
import com.firebase.ui.storage.images.FirebaseImageLoader
import com.google.firebase.storage.StorageReference
import pl.aprilapps.easyphotopicker.EasyImage
import java.io.InputStream
import java.util.*
import kotlin.random.Random

class EventApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // App Wide Initializations

        Random(Date().time)

        EasyImage.configuration(this)
            .setImagesFolderName(Constants.localImages)
            .setCopyTakenPhotosToPublicGalleryAppFolder(true)
            .setAllowMultiplePickInGallery(true)
    }

    override fun onTerminate() {
        EasyImage.clearConfiguration(this)
        super.onTerminate()
    }
}

