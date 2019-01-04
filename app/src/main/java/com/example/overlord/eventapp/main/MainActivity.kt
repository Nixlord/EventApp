package com.example.overlord.eventapp.main

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.util.Log
import com.example.overlord.eventapp.R
import com.example.overlord.eventapp.SecondActivity
import com.example.overlord.eventapp.abstractions.createCameraResultHandler
import com.example.overlord.eventapp.base.BaseActivity
import com.example.overlord.eventapp.extensions.*
import com.example.overlord.eventapp.model.Guest
import com.firebase.ui.auth.AuthUI
import kotlinx.android.synthetic.main.activity_main.*
import pl.aprilapps.easyphotopicker.EasyImage

class MainActivity : BaseActivity() {

    private val cameraModuleHandler = createCameraResultHandler({ image ->
            val compressed = compressImage(image)
            firebaseStorage.pushImage(compressed)
                .addOnSuccessListener { textViewInput.text = image.name }
        }, { error ->
            logError(error.message)
        }
    )

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                textViewInput.setText(R.string.title_home)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_events -> {
                textViewInput.setText(R.string.title_events)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_camera -> {
                textViewInput.setText(R.string.title_camera)

                EasyImage.openChooserWithDocuments(
                    this,
                    "Upload Photo",
                    1)


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
                        .addOnFailureListener { error -> logError(error.message) }
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
                .addOnFailureListener { error -> logError(error.message) }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        cameraModuleHandler(requestCode, resultCode, data)
    }
}