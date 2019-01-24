package com.example.overlord.eventapp.mechanisms.filters

import android.graphics.Bitmap
import com.zomato.photofilters.imageprocessors.Filter

/**
 * @author Varun on 01/07/15.
 */
class ThumbnailItem {
    var image: Bitmap? = null
    var filter: Filter

    init {
        image = null
        filter = Filter()
    }
}
