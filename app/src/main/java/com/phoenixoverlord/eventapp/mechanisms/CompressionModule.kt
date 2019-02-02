package com.phoenixoverlord.eventapp.mechanisms

import com.phoenixoverlord.eventapp.base.BaseActivity
import com.phoenixoverlord.eventapp.extensions.getDefaultFolder
import id.zelory.compressor.Compressor
import java.io.File


fun BaseActivity.compressImage(image : File, name : String) : File {

    // Do this in background
    val compressor = Compressor(this)
        .setDestinationDirectoryPath(getDefaultFolder().absolutePath)
        .setMaxWidth(1920)
        .setMaxHeight(1080)
        .setQuality(50)

    return compressor
        .compressToFile(image, name)
}