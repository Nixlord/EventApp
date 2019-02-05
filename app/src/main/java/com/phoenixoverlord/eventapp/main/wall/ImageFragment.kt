package com.phoenixoverlord.eventapp.main.wall


import android.os.Bundle
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.phoenixoverlord.eventapp.R
import com.phoenixoverlord.eventapp.base.BaseFragment
import com.phoenixoverlord.eventapp.extensions.*
import kotlinx.android.synthetic.main.fragment_image.*
import kotlinx.android.synthetic.main.fragment_image.view.*
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
        base.apply {

            loadImage(view.fullSizePhotoView, imageID!!)
            downloadImage(imageID!!) { file ->
                view.fullSizePhotoView.setOnLongClickListener {
                    safeIntentDispatch(
                        Intent(Intent.ACTION_SEND).apply {
                            putExtra(Intent.EXTRA_STREAM, getExternallyAccessibleURI(file))
                            type = "image/jpeg"
                        }
                    )
                    true
                }
            }
        }
    }
}
