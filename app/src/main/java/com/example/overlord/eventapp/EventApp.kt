package com.example.overlord.eventapp

import android.app.Application
import com.example.overlord.eventapp.model.Constants
import pl.aprilapps.easyphotopicker.EasyImage
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