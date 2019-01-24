package com.example.overlord.eventapp.mechanisms.filters

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.support.annotation.DrawableRes
import android.support.v7.widget.LinearLayoutManager
import com.example.overlord.eventapp.R
import com.example.overlord.eventapp.base.BaseActivity
import com.zomato.photofilters.SampleFilters
import com.zomato.photofilters.imageprocessors.Filter
import kotlinx.android.synthetic.main.activity_filter.*
import java.util.*

class FilterActivity : BaseActivity(){

    companion object {
        init {
            System.loadLibrary("NativeImageProcessor")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)

        filterImageView.setImageBitmap(
            Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(resources, R.drawable.photo),
                640, 640, false))

        initHorizontalList()
    }


    private fun initHorizontalList() {
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        layoutManager.scrollToPosition(0)
        thumbnailRecyclerView.layoutManager = layoutManager
        thumbnailRecyclerView.setHasFixedSize(true)
        bindDataToAdapter()
    }

    private fun getScaledBitmap(@DrawableRes resourceID: Int): Bitmap? {
        return Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(resources, resourceID),
                640, 640, false
            )
    }

    private fun bindDataToAdapter() {
        val context = this.application
        val handler = Handler()

        val r = Runnable {
            val thumbImage = getScaledBitmap(R.drawable.photo)

            val filters = arrayListOf(
                    Filter(),
                    SampleFilters.getStarLitFilter(),
                    SampleFilters.getBlueMessFilter(),
                    SampleFilters.getAweStruckVibeFilter(),
                    SampleFilters.getLimeStutterFilter(),
                    SampleFilters.getNightWhisperFilter()
            )

            val thumbnailItems = ArrayList<ThumbnailItem>(6)

            val t1 = ThumbnailItem()
            val t2 = ThumbnailItem()
            val t3 = ThumbnailItem()
            val t4 = ThumbnailItem()
            val t5 = ThumbnailItem()
            val t6 = ThumbnailItem()

            t1.image = thumbImage
            t2.image = thumbImage
            t3.image = thumbImage
            t4.image = thumbImage
            t5.image = thumbImage
            t6.image = thumbImage
            ThumbnailsManager.clearThumbs()
            ThumbnailsManager.addThumb(t1) // Original Image

            t2.filter = SampleFilters.getStarLitFilter()
            ThumbnailsManager.addThumb(t2)

            t3.filter = SampleFilters.getBlueMessFilter()
            ThumbnailsManager.addThumb(t3)

            t4.filter = SampleFilters.getAweStruckVibeFilter()
            ThumbnailsManager.addThumb(t4)

            t5.filter = SampleFilters.getLimeStutterFilter()
            ThumbnailsManager.addThumb(t5)

            t6.filter = SampleFilters.getNightWhisperFilter()
            ThumbnailsManager.addThumb(t6)

            val thumbs = ThumbnailsManager.processThumbs(context)

            val adapter = ThumbnailsAdapter(thumbs) { filter ->
                    filterImageView.setImageBitmap(
                        filter.processFilter(getScaledBitmap(R.drawable.photo)))
            }


            thumbnailRecyclerView.adapter = adapter
            adapter.notifyDataSetChanged()
        }
        handler.post(r)
    }


}
