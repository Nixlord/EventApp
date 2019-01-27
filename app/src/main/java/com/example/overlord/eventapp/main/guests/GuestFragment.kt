package com.example.overlord.eventapp.main.guests


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.overlord.eventapp.R
import kotlinx.android.synthetic.main.fragment_guests.*

import java.io.Serializable

class GuestFragment : Fragment() {

    //Declare your data here
    class FragmentInputs(val firstName: String = "Diksha", val surname: String = "Agarwal") : Serializable

    interface FragmentInteractor : Serializable {
        //Implement your methods here
        fun onButtonPressed(message: String)
    }

    private var inputs: FragmentInputs? = null
    private var interactor: FragmentInteractor? = null

    companion object {
        @JvmStatic
        fun newInstance(inputs: FragmentInputs?, interactor: FragmentInteractor) =
            GuestFragment().apply {
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
        return inflater.inflate(R.layout.fragment_guests, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewPagerAdapter = ViewPagerAdapter(fragmentManager)
        guest_side_view_pager.adapter = viewPagerAdapter
        guest_side_tab.setupWithViewPager(guest_side_view_pager)

    }

    inner class ViewPagerAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment? {
            var fragment: Fragment? = null
            if (position == 0) {
                fragment = BrideFragment()
            } else if (position == 1) {
                fragment = GroomFragment()
            }
            return fragment
        }

        override fun getCount(): Int {
            return 2
        }

        override fun getPageTitle(position: Int): CharSequence? {
            var title: String? = null
            if (position == 0) {
                title = "Bride"
            } else if (position == 1) {
                title = "Groom"
            }
            return title
        }
    }
}