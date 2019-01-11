package com.example.overlord.eventapp.intro

import android.content.Intent
import android.os.Bundle
import com.example.overlord.eventapp.R
import com.example.overlord.eventapp.base.BaseActivity
import com.example.overlord.eventapp.extensions.Firebase.auth
import com.example.overlord.eventapp.extensions.finishAndStart
import com.example.overlord.eventapp.extensions.logError
import com.example.overlord.eventapp.main.MainActivity
import com.example.overlord.eventapp.extensions.snackbar

import com.firebase.ui.auth.AuthUI
import java.util.*
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import kotlinx.android.synthetic.main.activity_login.*
import java.lang.Error


class LoginActivity : BaseActivity() {

    private fun startApp() {
        snackbar("Successful Sign In")
        finishAndStart(MainActivity::class.java)
    }

    fun createPhoneLoginIntent() : Intent {
        return AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(Arrays.asList(
                AuthUI.IdpConfig.PhoneBuilder().build()
            ))
            .setIsSmartLockEnabled(false)
            .build()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        if (auth.currentUser != null) {
            startApp()
        }
        else {
            buttonLogin.setOnClickListener {
                startActivityGetResult(
                    createPhoneLoginIntent()
                ).addOnSuccessListener {
                    startApp()

                }.addOnFailureListener { error, intent ->
                    logError(error)
                    val response = IdpResponse.fromResultIntent(intent)

                    if (response == null){
                        val message = "Sign In Cancelled"
                        logError(Error(message))
                        snackbar(message)
                    }
                    else if (response.error!!.errorCode == ErrorCodes.NO_NETWORK) {
                        val message = "No Internet Connection"
                        logError(Error(message))
                        snackbar(message)
                    }

                }
            }
        }
    }

    /*
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                startApp()
            }
            else {
                if (response == null) {
                    snackbar("sign_in_cancelled")
                    return
                }
                if (response.error!!.errorCode == ErrorCodes.NO_NETWORK) {
                    snackbar("no_internet_connection")
                    return
                }
                snackbar("unknown_error")
                Log.e(TAG, "Sign-in error: ", response.error)
            }
        }
    }
    */
}
