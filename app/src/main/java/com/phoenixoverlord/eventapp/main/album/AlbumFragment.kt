package com.phoenixoverlord.eventapp.main.album

import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.phoenixoverlord.eventapp.R
import com.phoenixoverlord.eventapp.base.BaseFragment
import com.phoenixoverlord.eventapp.extensions.getSimpleName
import com.phoenixoverlord.eventapp.extensions.inflate
import com.phoenixoverlord.eventapp.extensions.loadImage
import com.phoenixoverlord.eventapp.extensions.logError
import kotlinx.android.synthetic.main.album_item.view.*
import kotlinx.android.synthetic.main.fragment_album.*
import java.io.Serializable
import java.lang.Error

class AlbumFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_album, container, false)
    }

    // 964 967 8942
    inner class Album (val name : String,@DrawableRes val imageID : Int)



    inner class AlbumAdapter(val albumList : ArrayList<Album>) : RecyclerView.Adapter<AlbumAdapter.AlbumHolder>() {

        override fun getItemCount() = albumList.size

        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): AlbumHolder {
            return AlbumHolder(
                p0.inflate(R.layout.album_item)
            )
        }

        override fun onBindViewHolder(p0: AlbumHolder, p1: Int) {
            p0.bindItems(albumList[p1])
        }

        inner class AlbumHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
            fun bindItems(album : Album) {
                itemView.apply {
                    base.loadImage(album_image, album.imageID)
                    album_name.text = album.name

                    arrayOf(album_image, album_name).forEach {
                        it.setOnClickListener {
                            loadFragment(PhotosFragment.newInstance(album.name))
                        }
                    }
                }
            }
        }
    }

    val events = arrayListOf(
        Album("Pool Party",  R.drawable.event_pool),
        Album("Haldi",       R.drawable.event_haldi),
        Album("Mehendi",     R.drawable.event_mehendi),
        Album("Sangeet",     R.drawable.event_sangeet),
        Album("Wedding",     R.drawable.event_wedding),
        Album("Reception",   R.drawable.event_reception)
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        albumRecyclerView.layoutManager = GridLayoutManager(base, 2)
        albumRecyclerView.setHasFixedSize(true)
        albumRecyclerView.adapter = AlbumAdapter(events)
    }

    private fun loadFragment(fragment: Fragment) {
        fragmentManager?.beginTransaction()
            ?.replace(R.id.fragmentContainer, fragment, fragment.getSimpleName())
            ?.addToBackStack(fragment.getSimpleName())
            ?.commit() ?: logError(Error("Null Fragment Manager"))
    }
}