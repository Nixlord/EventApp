package com.example.overlord.eventapp.main.guests


import android.os.Bundle
import android.support.v4.app.Fragment

import com.example.overlord.eventapp.R
import java.io.Serializable

class GuestsFragment : Fragment() {

    //Declare your data here
    class FragmentInputs(val firstName: String = "Diksha", val surname: String = "Agarwal") : Serializable

    interface FragmentInteractor : Serializable {
        //Implement your methods here
        fun onSnackbarButtonPressed(message: String)

        fun onSwitchFragmentButtonPressed()
    }

    private lateinit var inputs: FragmentInputs
    private lateinit var interactor: FragmentInteractor

    companion object {
        @JvmStatic
        fun newInstance(inputs: FragmentInputs, interactor: FragmentInteractor) =
            GuestsFragment().apply {
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

        //Initialize Heavier things here because onCreateView and onViewCreated are called much more number of times
    }
}