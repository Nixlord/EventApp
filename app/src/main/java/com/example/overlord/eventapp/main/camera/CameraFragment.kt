package com.example.overlord.eventapp.main.camera

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.overlord.eventapp.R
import com.example.overlord.eventapp.base.BaseActivity
import kotlinx.android.synthetic.main.fragment_camera.*
import java.io.Serializable

class CameraFragment : Fragment() {

    class FragmentInputs(val firstName : String = "Shibasis", val surname : String = "Patnaik") : Serializable

    interface FragmentInteractor : Serializable {
        fun onSnackbarButtonPressed(message: String)
        fun onSwitchFragmentButtonPressed()
    }

    private lateinit var inputs: FragmentInputs
    private lateinit var interactor: FragmentInteractor

    companion object {
        @JvmStatic
        fun newInstance(inputs : FragmentInputs, interactor : FragmentInteractor) =
            CameraFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("inputs", inputs)
                    putSerializable("interactor", interactor)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inputs = arguments?.getSerializable("inputs") as FragmentInputs
        interactor = arguments?.getSerializable("interactor") as FragmentInteractor
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_camera, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fabNewPhoto.setOnClickListener {
            (activity as BaseActivity).apply {
                withPermissions(

                )
            }
        }
    }
}
