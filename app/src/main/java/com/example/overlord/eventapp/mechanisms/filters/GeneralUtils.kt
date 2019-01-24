package com.example.overlord.eventapp.mechanisms.filters

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Path

/**
 * @author Varun on 30/06/15.
 */
object GeneralUtils {
    fun generateCircularBitmap(input: Bitmap): Bitmap {

        val width = input.width
        val height = input.height
        val outputBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        val path = Path()
        path.addCircle(
            (width / 2).toFloat(), (height / 2).toFloat(), Math.min(width, height / 2).toFloat(), Path.Direction.CCW
        )

        val canvas = Canvas(outputBitmap)
        canvas.clipPath(path)
        canvas.drawBitmap(input, 0f, 0f, null)
        return outputBitmap
    }
}
