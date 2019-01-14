package com.example.overlord.eventapp.main

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.content.ContextCompat
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.example.overlord.eventapp.R
import com.example.overlord.eventapp.base.BaseActivity
import com.example.overlord.eventapp.extensions.logDebug
import com.example.overlord.eventapp.main.album.AlbumFragment
import com.example.overlord.eventapp.main.camera.CameraFragment
import com.example.overlord.eventapp.main.event.EventFragment
import com.example.overlord.eventapp.main.guests.GuestFragment
import com.example.overlord.eventapp.main.wall.WallFragment
import com.example.overlord.eventapp.utils.SwipeDisabledViewPager
import kotlinx.android.synthetic.main.activity_main.*
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter
import com.example.overlord.eventapp.extensions.loadFragment
import java.util.concurrent.ConcurrentHashMap
import kotlin.RuntimeException

class MainActivity : BaseActivity() {

    fun createWallFragment(inputs: WallFragment.FragmentInputs?) : WallFragment {
        return WallFragment.newInstance(
            inputs,
            object : WallFragment.FragmentInteractor {
                override fun onButtonPressed(message: String) {
                    logDebug("snackbar pressed")
                }
            }
        )
    }
    fun createEventFragment(inputs: EventFragment.FragmentInputs?) : EventFragment {
        return EventFragment.newInstance(
            inputs,
            object : EventFragment.FragmentInteractor {
                override fun onButtonPressed(message: String) {
                    logDebug("snackbar pressed")
                }
            }
        )
    }

    fun createCameraFragment(inputs : CameraFragment.FragmentInputs?) : CameraFragment {
        return CameraFragment.newInstance(
            inputs,
            object : CameraFragment.FragmentInteractor {
                override fun onImageUploaded(postID : String) {

                }
            }
        )
    }

    fun createAlbumFragment(inputs : AlbumFragment.FragmentInputs?) : AlbumFragment {
        return AlbumFragment.newInstance(
            inputs,
            object : AlbumFragment.FragmentInteractor {
                override fun onButtonPressed(message: String) {
                    logDebug("snackbar pressed")
                }
            }
        )
    }

    fun createGuestsFragment(inputs : GuestFragment.FragmentInputs?) : GuestFragment {
        return GuestFragment.newInstance(
            inputs,
            object : GuestFragment.FragmentInteractor {
                override fun onButtonPressed(message: String) {
                    logDebug("snackbar pressed")
                }
            }
        )
    }

    private fun setupBottomNavigation(bottomNavigation : AHBottomNavigation, menuID : Int, colorsID : Int) {
        /*
        Set Bottom Navigation colors. Accent color for active item,
        Inactive color when its view is disabled.

        Will not be visible if setColored(true) and default current item is set.
         */
        fun getUserColor(id : Int) = ContextCompat.getColor(this, id)
        bottomNavigation.apply {
            defaultBackgroundColor = Color.WHITE
            accentColor = getUserColor(R.color.colorAccent)
            inactiveColor = Color.WHITE
            isColored = true
            setColoredModeColors(
                Color.WHITE,
                Color.GRAY
            )
            titleState = AHBottomNavigation.TitleState.ALWAYS_SHOW
        }

        val navigationAdapter = AHBottomNavigationAdapter(this, menuID)
        navigationAdapter.setupWithBottomNavigation(bottomNavigation, applicationContext.resources.getIntArray(colorsID))
    }


    private val fragments : MutableMap<String, Fragment> = ConcurrentHashMap()
    private val fragmentNames = arrayListOf("wall", "event", "camera", "album", "guest")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fragments["wall"] =  createWallFragment(null)
        fragments["event"] =  createEventFragment(null)
        fragments["camera"] =  createCameraFragment(null)
        fragments["album"] =  createAlbumFragment(null)
        fragments["guest"] =  createGuestsFragment(null)


        setupBottomNavigation(bottomNavigation, R.menu.navigation, R.array.colors)
        bottomNavigation.setOnTabSelectedListener { position, wasSelected ->
            if ( ! wasSelected )
                loadFragment(R.id.fragmentContainer,
                    fragments[fragmentNames[position]] ?: throw RuntimeException("$position not found"))
            true
        }
        bottomNavigation.currentItem = 2
    }
}
