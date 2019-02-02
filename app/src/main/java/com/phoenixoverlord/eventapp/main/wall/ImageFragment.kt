package com.phoenixoverlord.eventapp.main.wall


import android.os.Bundle
import android.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.phoenixoverlord.eventapp.R
import com.phoenixoverlord.eventapp.base.BaseFragment
import com.phoenixoverlord.eventapp.extensions.loadImage
import com.phoenixoverlord.eventapp.extensions.logError
import kotlinx.android.synthetic.main.fragment_image.*
import java.lang.Error


class ImageFragment : BaseFragment() {

    private var imageID : String? = null

    companion object {
        @JvmStatic
        fun newInstance(imageID : String) =
            ImageFragment().apply {
                arguments = Bundle().apply { putString("imageID", imageID) }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imageID = arguments?.getString("imageID")
        if (imageID == null)
            logError(Error("Null Image ID"))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imageID?.apply {
            base.loadImage(fullSizePhotoView, this)
        }
    }
}
