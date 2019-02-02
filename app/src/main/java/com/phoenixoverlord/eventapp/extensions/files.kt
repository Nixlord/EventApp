package com.phoenixoverlord.eventapp.extensions

import android.os.Environment
import com.phoenixoverlord.eventapp.model.Constants
import com.phoenixoverlord.eventapp.utils.uniqueName
import java.io.File


fun getDefaultFolder() : File {
    val destinationRoot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
    return File(destinationRoot, Constants.localCompressedImages)
}

fun createImageFile(fileName : String = uniqueName(), parentFolder : File = getDefaultFolder()) : File {
    return File(parentFolder, fileName)
}