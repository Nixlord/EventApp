package com.example.overlord.eventapp.main.guests

import android.os.Bundle
import android.support.v4.app.Fragment
import java.io.Serializable

class GroomFragment : Fragment() {

    class FragmentInputs(val firstName: String = "Diksha", val surname: String = "Agarwal") : Serializable

    interface FragmentInteractor : Serializable {
        //Implement your methods here
        fun onButtonPressed(message: String)
    }

    private var inputs: GroomFragment.FragmentInputs? = null
    private var interactor: FragmentInteractor? = null

    companion object {
        @JvmStatic
        fun newInstance(inputs: GroomFragment.FragmentInputs?, interactor: FragmentInteractor) =
            GroomFragment().apply {this.interactor = interactor
                arguments = Bundle().apply { putSerializable("inputs", inputs) }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inputs = arguments?.getSerializable("inputs") as GroomFragment.FragmentInputs?
        //Initialize Heavier things here because onCreateView and onViewCreated are called much more number of times
    }
}