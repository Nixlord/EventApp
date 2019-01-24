package com.example.overlord.eventapp.main.album

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.res.ResourcesCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.overlord.eventapp.R
import com.example.overlord.eventapp.base.BaseFragment
import ja.burhanrashid52.photoeditor.PhotoEditor
import kotlinx.android.synthetic.main.fragment_album.*
import java.io.Serializable

class AlbumFragment : BaseFragment() {

    //Declare your data here
    class FragmentInputs(val firstName: String = "Diksha", val surname: String = "Agarwal") : Serializable

    interface FragmentInteractor : Serializable {
        //Implement your methods here
        fun onButtonPressed(message: String)
    }

    private var inputs: FragmentInputs? = null
    private var interactor: FragmentInteractor?  = null

    companion object {
        @JvmStatic
        fun newInstance(inputs: FragmentInputs?, interactor: FragmentInteractor) =
            AlbumFragment().apply {
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
        return inflater.inflate(R.layout.fragment_album, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        placeholderView.text = "SHIBASIS"
    }
}