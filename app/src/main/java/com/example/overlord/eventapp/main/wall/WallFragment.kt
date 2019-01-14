package com.example.overlord.eventapp.main.wall


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide

import com.example.overlord.eventapp.R
import com.example.overlord.eventapp.base.BaseFragment
import com.example.overlord.eventapp.extensions.Firebase.storage
import com.example.overlord.eventapp.extensions.logError
import com.example.overlord.eventapp.extensions.toastSuccess
import com.example.overlord.eventapp.model.Constants
import kotlinx.android.synthetic.main.fragment_camera.*
import java.io.Serializable
import java.lang.Error

class WallFragment : BaseFragment() {

    //Declare your data here
    class FragmentInputs(val postID: String? = null) : Serializable

    interface FragmentInteractor : Serializable {
        //Implement your methods here
        fun onButtonPressed(message: String)
    }

    private var inputs: FragmentInputs? = null
    private var interactor: FragmentInteractor? = null

    companion object {
        @JvmStatic
        fun newInstance(inputs: FragmentInputs?, interactor: FragmentInteractor) =
            WallFragment().apply {
                this.interactor = interactor
                arguments = Bundle().apply { putSerializable("inputs", inputs) }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inputs = arguments?.getSerializable("inputs") as FragmentInputs?
        //Initialize Heavier things here because onCreateView and onViewCreated are called much more number of times
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_wall, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        inputs?.postID?.apply {

            val imageRef = storage.child(Constants.remoteCompressedImages).child(this)
            Glide.with(base).load(imageRef).into(photoView)
            base.toastSuccess("HeHeHe")

        } ?: logError(Error("PostID null"))

    }
}