package com.example.overlord.eventapp.mechanisms.filters

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.overlord.eventapp.R
import com.zomato.photofilters.imageprocessors.Filter

/**
 * @author Varun on 01/07/15.
 */
class ThumbnailsAdapter(private val dataSet: List<ThumbnailItem>, private val thumbnailCallback: (Filter) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    init {
        Log.v(TAG, "Thumbnails Adapter has " + dataSet.size + " items")
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): RecyclerView.ViewHolder {
        Log.v(TAG, "On Create View Holder Called")
        val itemView = LayoutInflater.from(viewGroup.context).inflate(R.layout.list_thumbnail_item, viewGroup, false)
        return ThumbnailsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, i: Int) {
        val thumbnailItem = dataSet[i]
        Log.v(TAG, "On Bind View Called")
        val thumbnailsViewHolder = holder as ThumbnailsViewHolder
        thumbnailsViewHolder.thumbnail.setImageBitmap(thumbnailItem.image)
        thumbnailsViewHolder.thumbnail.scaleType = ImageView.ScaleType.FIT_START
        lastPosition = i
        thumbnailsViewHolder.thumbnail.setOnClickListener {
            if (lastPosition != i) {
                thumbnailCallback(thumbnailItem.filter)
                lastPosition = i
            }
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    inner class ThumbnailsViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var thumbnail: ImageView

        init {
            this.thumbnail = v.findViewById<View>(R.id.thumbnail) as ImageView
        }
    }

    companion object {
        private val TAG = "THUMBNAILS_ADAPTER"
        private var lastPosition = -1
    }
}
