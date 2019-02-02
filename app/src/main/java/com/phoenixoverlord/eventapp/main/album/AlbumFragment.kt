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
        super.onViewCreated(view, savedInstanceState)

        //Pool Party
        arrayOf(
            pool_party_image,
            pool_party_name
        ).forEach {
            it.setOnClickListener {
                loadFragment(PhotosFragment.newInstance("Pool Party"))
            }
        }

        //Haldi
        arrayOf(
            haldi_image,
            haldi_name
        ).forEach {
            it.setOnClickListener {
                loadFragment(PhotosFragment.newInstance("Haldi"))
            }
        }

        //Mehendi
        arrayOf(
            mehendi_image,
            mehendi_name
        ).forEach {
            it.setOnClickListener {
                loadFragment(PhotosFragment.newInstance("Mehendi"))
            }
        }

        //Sangeet
        arrayOf(
            sangeet_image,
            sangeet_name
        ).forEach {
            it.setOnClickListener {
                loadFragment(PhotosFragment.newInstance("Sangeet"))
            }
        }

        //Wedding
        arrayOf(
            wedding_image,
            wedding_name
        ).forEach {
            it.setOnClickListener {
                loadFragment(PhotosFragment.newInstance("Wedding"))
            }
        }

        //Reception
        arrayOf(
            reception_image,
            reception_name
        ).forEach {
            it.setOnClickListener {
                loadFragment(PhotosFragment.newInstance("Reception"))
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