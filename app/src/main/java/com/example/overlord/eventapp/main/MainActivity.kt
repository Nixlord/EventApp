package com.example.overlord.eventapp.main

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import com.example.overlord.eventapp.R
import com.example.overlord.eventapp.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity() {


    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->

        when (item.itemId) {
            R.id.navigation_wall -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_events -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_camera -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_albums -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_guests -> {
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }
     fun setupBottomNavigationUI() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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