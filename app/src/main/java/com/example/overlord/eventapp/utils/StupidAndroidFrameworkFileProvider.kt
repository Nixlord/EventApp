package com.example.overlord.eventapp.utils

import android.net.Uri
import android.support.v4.content.FileProvider

public class StupidAndroidFrameworkFileProvider : FileProvider() {
    override fun getType(uri: Uri): String {
        return "images/jpeg"
    }
}