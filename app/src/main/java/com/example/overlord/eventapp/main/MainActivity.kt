package com.example.overlord.eventapp.main

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
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
import java.lang.RuntimeException
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter




class MainActivity : BaseActivity() {

    fun createWallFragment() : WallFragment {
        return WallFragment.newInstance(
            WallFragment.FragmentInputs(),
            object : WallFragment.FragmentInteractor {
                override fun onButtonPressed(message: String) {
                    logDebug("snackbar pressed")
                }
            }
        )
    }
    fun createEventFragment() : EventFragment {
        return EventFragment.newInstance(
            EventFragment.FragmentInputs(),
            object : EventFragment.FragmentInteractor {
                override fun onButtonPressed(message: String) {
                    logDebug("snackbar pressed")
                }
            }
        )
    }

    fun createCameraFragment() : CameraFragment {
        return CameraFragment.newInstance(
            CameraFragment.FragmentInputs(),
            object : CameraFragment.FragmentInteractor {
                override fun onButtonPressed(message: String) {
                    logDebug("snackbar pressed")
                }
            }
        )
    }

    fun createAlbumFragment() : AlbumFragment {
        return AlbumFragment.newInstance(
            AlbumFragment.FragmentInputs(),
            object : AlbumFragment.FragmentInteractor {
                override fun onButtonPressed(message: String) {
                    logDebug("snackbar pressed")
                }
            }
        )
    }

    fun createGuestsFragment() : GuestFragment {
        return GuestFragment.newInstance(
            GuestFragment.FragmentInputs(),
            object : GuestFragment.FragmentInteractor {
                override fun onButtonPressed(message: String) {
                    logDebug("snackbar pressed")
                }
            }
        )
    }


    private fun setupViewPager(viewPager: SwipeDisabledViewPager) {

        viewPager.apply {

            shouldInterceptTouch = false

            adapter = object : FragmentStatePagerAdapter(supportFragmentManager) {
                override fun getItem(p0: Int): Fragment = when(p0) {
                    0 -> createWallFragment()
                    1 -> createEventFragment()
                    2 -> createCameraFragment()
                    3 -> createAlbumFragment()
                    4 -> createGuestsFragment()

                    else -> throw RuntimeException("No Such Fragment")
                }

                override fun getCount() = 5
            }
        }
    }

    fun getUserColor(id : Int) = ContextCompat.getColor(this, id)

    private fun setupBottomNavigation(bottomNavigation : AHBottomNavigation, menuID : Int, colorsID : Int) {
        /*
        Set Bottom Navigation colors. Accent color for active item,
        Inactive color when its view is disabled.

        Will not be visible if setColored(true) and default current item is set.
         */

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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        supportActionBar?.title = "Snigdha Weds Charan "
        setupViewPager(viewPager)
        setupBottomNavigation(bottomNavigation, R.menu.navigation, R.array.colors)
        bottomNavigation.setOnTabSelectedListener { position, wasSelected ->
            if (!wasSelected)
                viewPager.currentItem = position
            true
        }
    }
}

/*

                /**
                 * Hello Callback hell
                 * Get out by learning RxJava
                 */
                withPermissions(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).execute({
                    takePhoto("Upload to Firebase")
                        .addOnSuccessListener { image ->

                            logDebug("Cache Name: ${image.name}")

                            val imageName = uniqueName()

                            val compressed = compressImage(image, imageName)
                            storage.pushImage(compressed, imageName)
                                .addOnSuccessListener {
                                    logDebug("Uploaded ${image.name}")
                                }

                        }
                }, this::logError)

 */