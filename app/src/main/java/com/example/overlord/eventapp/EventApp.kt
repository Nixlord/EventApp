package com.example.overlord.eventapp

import android.app.Application
import pl.aprilapps.easyphotopicker.EasyImage

class EventApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // App Wide Initializations

        EasyImage.configuration(this)
            .setImagesFolderName("Snigdha_Charan")
            .setCopyTakenPhotosToPublicGalleryAppFolder(true)

    }
}