package com.example.overlord.eventapp.intro

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.example.overlord.eventapp.R
import com.example.overlord.eventapp.base.BaseActivity
import com.example.overlord.eventapp.extensions.Firebase.auth
import com.example.overlord.eventapp.extensions.Firebase.firestore
import com.example.overlord.eventapp.extensions.finishAndStart
import com.example.overlord.eventapp.extensions.logDebug
import com.example.overlord.eventapp.extensions.logError
import com.example.overlord.eventapp.main.MainActivity
import com.example.overlord.eventapp.extensions.snackbar

import com.example.overlord.eventapp.model.User
import com.example.overlord.eventapp.model.relationships

import com.firebase.ui.auth.AuthUI
import java.util.*
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.jaredrummler.materialspinner.MaterialSpinner
import kotlinx.android.synthetic.main.activity_login.*
import java.lang.Error
import kotlin.collections.ArrayList


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
                    val user = auth.currentUser


                    /*

                    You can get these two parameters from the User object
                    user!!.uid
                    user!!.phoneNumber

                    object!!.parameter implies that we know this object is not null
                    This is required sometimes when a function returns a nullable type
                    But dynamic application logic ensures this is not null
                    Since the logic is dynamic, static analysis won't flag it as not null
                    Hence we mark it manually.


                    Take these parameters and others you input from editTexts and create User (model) object

                    Then you can save like this
                    firestore.collection("users")
                        .document(user!!.uid).set(User(<insert data from editText and user auth object>))
                    */


                    firestore.collection("users")
                    startApp()

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

    fun setUpViews() {


        user.name = userName.text.toString()

        bride.setOnClickListener { user.wedding_side = "Bride" }
        groom.setOnClickListener { user.wedding_side = "Groom" }

        user.relation = "Friend"
        setupSpinner(userRelation, R.array.relations) { selected -> user.relation = selected }

    }

}
