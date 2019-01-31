package com.phoenixoverlord.eventapp.mechanisms.filters

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.support.annotation.DrawableRes
import android.support.v7.widget.LinearLayoutManager
import com.phoenixoverlord.eventapp.R
import com.phoenixoverlord.eventapp.base.BaseActivity
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
        filterImageView.setImageBitmap(getScaledBitmap(R.drawable.photo))
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
        Handler().post {
            val thumbImage = getScaledBitmap(R.drawable.photo)

            val filters = arrayListOf(
                    Filter(),
                    SampleFilters.getStarLitFilter(),
                    SampleFilters.getBlueMessFilter(),
                    SampleFilters.getAweStruckVibeFilter(),
                    SampleFilters.getLimeStutterFilter(),
                    SampleFilters.getNightWhisperFilter()
            )

            val thumbnailItems = ArrayList<ThumbnailItem>()

            ThumbnailsManager.clearThumbs()

            filters.forEach {
                val item = ThumbnailItem(thumbImage, it)

                thumbnailItems.add(item)
                ThumbnailsManager.addThumb(item)
            }

            val thumbs = ThumbnailsManager.processThumbs(this@FilterActivity)

            val adapter = ThumbnailsAdapter(thumbs) { filter ->
                    filterImageView.setImageBitmap(
                        filter.processFilter(getScaledBitmap(R.drawable.photo)))
            }

            thumbnailRecyclerView.adapter = adapter
            adapter.notifyDataSetChanged()
        }
    }
}
