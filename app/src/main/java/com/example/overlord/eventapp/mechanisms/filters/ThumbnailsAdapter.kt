package com.example.overlord.eventapp.mechanisms.filters

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.overlord.eventapp.R
import com.example.overlord.eventapp.extensions.inflate
import com.example.overlord.eventapp.extensions.logDebug
import com.zomato.photofilters.imageprocessors.Filter
import kotlinx.android.synthetic.main.list_thumbnail_item.view.*

/**
 * @author Varun on 01/07/15.
 */
class ThumbnailsAdapter(private val dataSet: List<ThumbnailItem>, private val thumbnailCallback: (Filter) -> Unit) :
    RecyclerView.Adapter<ThumbnailsAdapter.ThumbnailViewHolder>() {

    init {
        logDebug(TAG, "Thumbnails Adapter has " + dataSet.size + " items")
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ThumbnailViewHolder {
        return ThumbnailViewHolder(viewGroup.inflate(R.layout.list_thumbnail_item))
    }

    override fun onBindViewHolder(holder: ThumbnailViewHolder, i: Int) {
        lastPosition = i
        val thumbnailItem = dataSet[i]

        holder.itemView.thumbnailView.apply {
            setImageBitmap(thumbnailItem.image)
            scaleType = ImageView.ScaleType.FIT_START
            setOnClickListener {
                if (lastPosition != i) {
                    thumbnailCallback(thumbnailItem.filter)
                    lastPosition = i
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    inner class ThumbnailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    companion object {
        private val TAG = "THUMBNAILS_ADAPTER"
        private var lastPosition = -1
    }
}
