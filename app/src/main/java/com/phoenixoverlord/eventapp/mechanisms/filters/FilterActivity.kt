package com.phoenixoverlord.eventapp.mechanisms.filters

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.support.annotation.DrawableRes
import android.support.v7.widget.LinearLayoutManager
import com.phoenixoverlord.eventapp.R
import com.phoenixoverlord.eventapp.base.BaseActivity
import com.phoenixoverlord.eventapp.extensions.loadImage
import com.phoenixoverlord.eventapp.extensions.toastError
import com.zomato.photofilters.SampleFilters
import com.zomato.photofilters.imageprocessors.Filter
import kotlinx.android.synthetic.main.activity_filter.*
import java.io.File
import java.util.*

class FilterActivity : BaseActivity(){

    companion object {
        init {
            System.loadLibrary("NativeImageProcessor")
        }
        fun newIntent(activity: BaseActivity, image : File) : Intent {
            return Intent(activity, FilterActivity::class.java).apply {
                putExtra("imageFile", image)
            }
        }
    }

    private var imageFile : File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)

        imageFile = intent.getSerializableExtra("imageFile") as File?

        if (imageFile == null) {
            toastError("Null Image")
            finish()
        }

        loadImage(filterImageView, imageFile!!)
        initHorizontalList()
        bindDataToAdapter(imageFile!!)
    }


    private fun initHorizontalList() {
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        layoutManager.scrollToPosition(0)
        thumbnailRecyclerView.layoutManager = layoutManager
        thumbnailRecyclerView.setHasFixedSize(true)
    }



    private fun getScaledBitmap(image : File): Bitmap? {
        return Bitmap.createScaledBitmap(
            BitmapFactory.decodeFile(image.absolutePath),
            640, 640, false
        )
    }

    private fun bindDataToAdapter(image : File) {
        Handler().post {
            val thumbImage = getScaledBitmap(image)

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
                        filter.processFilter(getScaledBitmap(image)))
            }

            thumbnailRecyclerView.adapter = adapter
            adapter.notifyDataSetChanged()
        }
    }
}
