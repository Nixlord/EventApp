package com.example.overlord.eventapp

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.util.Log
import com.firebase.ui.auth.AuthUI
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*
import java.util.concurrent.TimeUnit
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse



class Login : AppCompatActivity() {

    val REQUEST_CODE = 1
    val TAG = "LoginActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (FirebaseAuth.getInstance().currentUser != null) {
            startApp()
        }
        else {
            startActivityForResult(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(Arrays.asList(
                        AuthUI.IdpConfig.GoogleBuilder().build()
                    ))
                    .setIsSmartLockEnabled(false)
                    .build(),
                REQUEST_CODE
            )
        }

        snackbar("Tets")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                startApp()
            } else {
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

    fun startApp() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
