package com.phoenixoverlord.eventapp.main.guests


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.phoenixoverlord.eventapp.R
import com.phoenixoverlord.eventapp.base.BaseFragment
import com.phoenixoverlord.eventapp.extensions.toastError
import kotlinx.android.synthetic.main.fragment_guests.*

import java.lang.IllegalArgumentException

class GuestFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_guests, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewPagerAdapter = ViewPagerAdapter(childFragmentManager)
        guest_side_view_pager.adapter = viewPagerAdapter
        guest_side_tab.setupWithViewPager(guest_side_view_pager)
    }

    inner class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return when(position) {
                0 -> BrideGroomFragment.newInstance("Bride")
                1 -> BrideGroomFragment.newInstance("Groom")
                else -> throw IllegalArgumentException("Illegal Page Position $position")
            }
        }

        override fun getCount(): Int {
            return 2
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return when(position) {
                0 -> "Bride"
                1 -> "Groom"
                else -> throw IllegalArgumentException("Illegal Page Position $position")
            }
        }
    }
}