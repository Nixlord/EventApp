package com.phoenixoverlord.eventapp.main.album

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.phoenixoverlord.eventapp.R
import com.phoenixoverlord.eventapp.base.BaseFragment
import com.phoenixoverlord.eventapp.extensions.getSimpleName
import com.phoenixoverlord.eventapp.extensions.logError
import kotlinx.android.synthetic.main.fragment_album.*
import java.io.Serializable
import java.lang.Error

class AlbumFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_album, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arrayOf(
            Pair("Pool Party",  arrayOf(pool_party_image, pool_party_name)),
            Pair("Haldi",       arrayOf(haldi_image, haldi_name)),
            Pair("Mehendi",     arrayOf(mehendi_image, mehendi_name)),
            Pair("Sangeet",     arrayOf(sangeet_image, sangeet_name)),
            Pair("Wedding",     arrayOf(wedding_image, wedding_name)),
            Pair("Reception",   arrayOf(reception_image, reception_name ))
        ).forEach {
            val (eventTag, views) = it
            views.forEach {
                it.setOnClickListener {
                    loadFragment(PhotosFragment.newInstance(eventTag))
                }
            }
        }

    }

    private fun loadFragment(fragment: Fragment) {
        fragmentManager?.beginTransaction()
            ?.replace(R.id.fragmentContainer, fragment, fragment.getSimpleName())
            ?.addToBackStack(fragment.getSimpleName())
            ?.commit() ?: logError(Error("Null Fragment Manager"))
    }
}