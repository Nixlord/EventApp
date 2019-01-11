package com.example.overlord.eventapp.main

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import com.example.overlord.eventapp.R
import com.example.overlord.eventapp.xtra.SecondActivity
import com.example.overlord.eventapp.base.BaseActivity
import com.example.overlord.eventapp.extensions.*
import com.example.overlord.eventapp.mechanisms.compressImage
import com.example.overlord.eventapp.model.Guest
import com.firebase.ui.auth.AuthUI
import kotlinx.android.synthetic.main.activity_main.*

/*
abstract class MainActivityBackup : BaseActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_wall -> {
                textViewInput.setText(R.string.title_wall)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_events -> {
                textViewInput.setText(R.string.title_events)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_camera -> {
                textViewInput.setText(R.string.title_camera)

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
                            logDebug("Name: ${image.name}")
                            val compressed = compressImage(image)
                            firebaseStorage.pushImage(compressed)
                                .addOnSuccessListener { logDebug("Uploaded ${image.name}") }

                        }
                }, this::logError)

                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_albums -> {
                textViewInput.setText(R.string.title_albums)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_guests -> {
                textViewInput.setText(R.string.title_guests)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        editTextName.onTextChange { input ->
            textViewInput.text = input
            firebaseAuth?.currentUser?.email?.let {
                firestore.collection("users")
                        .add(Guest(
                                input,
                                it
                        ))
                        .addOnFailureListener { exception -> logError("FirestoreSaveFail", exception) }
            }
        }

        buttonSignOut.setOnClickListener {
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener { finish() }
        }

        buttonNewActivity.setOnClickListener {
            startActivityGetResult(Intent(this, SecondActivity::class.java))
                .addOnSuccessListener { intent -> logDebug(intent.getStringExtra("Name")) }
                .addOnFailureListener(this::logError)
        }


    }
}
        */