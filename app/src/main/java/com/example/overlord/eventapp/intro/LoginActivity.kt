package com.example.overlord.eventapp.intro

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.overlord.eventapp.R
import com.example.overlord.eventapp.base.BaseActivity
import com.example.overlord.eventapp.extensions.*
import com.example.overlord.eventapp.extensions.Firebase.auth
import com.example.overlord.eventapp.extensions.Firebase.firestore
import com.example.overlord.eventapp.extensions.Firebase.storage
import com.example.overlord.eventapp.main.MainActivity
import com.example.overlord.eventapp.mechanisms.compressImage

import com.example.overlord.eventapp.model.User
import com.example.overlord.eventapp.utils.uniqueName

import com.firebase.ui.auth.AuthUI
import java.util.*
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import kotlinx.android.synthetic.main.activity_login.*
import java.lang.Error

class LoginActivity : BaseActivity() {

    private var user = User()

    private fun createPhoneLoginIntent(): Intent {
        return AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(
                Arrays.asList(
                    AuthUI.IdpConfig.PhoneBuilder().build()
                )
            )
            .setIsSmartLockEnabled(false)
            .build()
    }

    private fun startApp() {
        snackbar("Successful Sign In")
        finishAndStart(MainActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setUpViews()

        if (auth.currentUser != null) {
            startApp()
        } else {
            buttonLogin.setOnClickListener {

                startActivityGetResult(
                    createPhoneLoginIntent()

                ).addOnSuccessListener {
                    user.phoneno = auth.currentUser!!.phoneNumber.toString()
                    firestore.collection("users")
                        .add(user)
                        .addOnSuccessListener { documentReference ->
                            logDebug("DocumentSnapshot added with ID: " + documentReference.id)
                            startApp()
                        }
                        .addOnFailureListener { e ->
                            logError("Error adding document", e)
                        }
                }.addOnFailureListener { error, intent ->
                    logError(error)
                    val response = IdpResponse.fromResultIntent(intent)

                    if (response == null) {
                        val message = "Sign In Cancelled"
                        logError(Error(message))
                        snackbar(message)
                    } else if (response.error!!.errorCode == ErrorCodes.NO_NETWORK) {
                        val message = "No Internet Connection"
                        logError(Error(message))
                        snackbar(message)
                    }

                }
            }
        }
    }

    fun setupSpinner(spinner : Spinner, textArrayResID : Int, onItemSelected : (String) -> Unit) {
        ArrayAdapter.createFromResource(
            this,
            textArrayResID,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                logDebug("Nothing Selected")
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                onItemSelected(parent?.getItemAtPosition(position) as String)
            }
        }
    }

    private fun setUpViews() {

        userName.onTextChange { text -> user.name = text }

        bride.setOnClickListener {
            groom.setImageResource(R.drawable.male_bw)
            bride.setImageResource(R.drawable.female)
            user.wedding_side = "Bride"
        }

        groom.setOnClickListener {
            bride.setImageResource(R.drawable.female_bw)
            groom.setImageResource(R.drawable.male)
            user.wedding_side = "Groom"
        }

        user.relation = "Friend"
        setupSpinner(userRelation, R.array.relations) { selected -> user.relation = selected }

        uploadProfilePhoto.setOnClickListener {
            withPermissions(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            ).execute({
                takePhoto("Add Profile Photo")
                    .addOnSuccessListener { image ->

                        logDebug("Profile photo name: ${image.name}")

                        user.profile_photo = uniqueName()

                        val compressed = compressImage(image, user.profile_photo)
                        storage.pushImage(compressed, user.profile_photo)
                            .addOnSuccessListener {
                                Glide.with(this).load(compressed).into(userProfilePhoto)
                                logDebug("Uploaded ${image.name}")
                            }

                    }
            }, this::logError)
        }

    }

}
