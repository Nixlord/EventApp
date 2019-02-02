package com.phoenixoverlord.eventapp.main

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.phoenixoverlord.eventapp.R
import com.phoenixoverlord.eventapp.base.BaseActivity
import com.phoenixoverlord.eventapp.main.album.AlbumFragment
import com.phoenixoverlord.eventapp.main.camera.CameraFragment
import com.phoenixoverlord.eventapp.main.event.EventFragment
import com.phoenixoverlord.eventapp.main.guests.GuestFragment
import com.phoenixoverlord.eventapp.main.wall.WallFragment
import kotlinx.android.synthetic.main.activity_main.*
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter
import com.phoenixoverlord.eventapp.extensions.*
import com.phoenixoverlord.eventapp.extensions.Firebase.auth
import com.phoenixoverlord.eventapp.intro.LoginActivity

import com.phoenixoverlord.eventapp.main.wall.PostFragment
import com.phoenixoverlord.eventapp.mechanisms.filters.FilterActivity
import com.phoenixoverlord.eventapp.model.Post
import java.util.concurrent.ConcurrentHashMap
import kotlin.RuntimeException

class MainActivity : BaseActivity() {

    fun createWallFragment(inputs: WallFragment.FragmentInputs? = null) : WallFragment {
        return WallFragment.newInstance(
            inputs,
            object : WallFragment.FragmentInteractor {
                override fun addPostFragment(post: Post) {
                    logDebug("Opening Post Fragment")
                    addFragment(R.id.fragmentContainer, PostFragment())
                }
            }
        )
    }
    fun createCameraFragment(inputs : CameraFragment.FragmentInputs? = null) : CameraFragment {
        return CameraFragment.newInstance(
            inputs,
            object : CameraFragment.FragmentInteractor {
                override fun onImageUploaded(postID : String) {
                    fragments["wall"] = createWallFragment(
                        WallFragment.FragmentInputs(
                            postID = postID
                        ))
                    bottomNavigation.currentItem = fragmentNames.indexOf("wall")
                }

                override fun onEditButtonClicked() {
                    toastSuccess("Edit Button Clicked")
                    startActivity(Intent(this@MainActivity, FilterActivity::class.java))
                }
            }
        )
    }

    fun createEventFragment() : EventFragment {
        return EventFragment()
    }

    fun createAlbumFragment() : AlbumFragment {
        return AlbumFragment()
    }

    fun createGuestsFragment() : GuestFragment {
        return GuestFragment()
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

        if (auth.currentUser == null) {
            finishAndStart(LoginActivity::class.java)
        }

        fragments["wall"]   =  createWallFragment()
        fragments["event"]  =  createEventFragment()
        fragments["camera"] =  createCameraFragment()
        fragments["album"]  =  createAlbumFragment()
        fragments["guest"]  =  createGuestsFragment()

        setupBottomNavigation(bottomNavigation, R.menu.navigation, R.array.colors)
        bottomNavigation.setOnTabSelectedListener { position, wasSelected ->
            if ( ! wasSelected )
                replaceFragment(R.id.fragmentContainer,
                    fragments[fragmentNames[position]] ?: throw RuntimeException("$position not found"))
            true
        }

        bottomNavigation.currentItem = 2

    }
}
