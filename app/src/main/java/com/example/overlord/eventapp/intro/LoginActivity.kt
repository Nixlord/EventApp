package com.example.overlord.eventapp.intro

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.overlord.eventapp.R
import com.example.overlord.eventapp.main.MainActivity
import com.example.overlord.eventapp.common.snackbar

import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import java.util.*
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    val REQUEST_CODE = 1
    val TAG = "LoginActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        checkFirstRun()

        setContentView(R.layout.activity_login)

        if (FirebaseAuth.getInstance().currentUser != null) {
            startApp()
        }
        else {
            buttonLogin.setOnClickListener {
                startActivityForResult(
                    AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(Arrays.asList(
                            AuthUI.IdpConfig.PhoneBuilder().build()
                        ))
                        .setIsSmartLockEnabled(false)
                        .build(),
                    REQUEST_CODE
                )
            }
        }


    }

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

    fun startApp() {
        snackbar("Successful Sign In")
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    /*
    ToDo @Diksha Delete this after understanding logic
    fun checkFirstRun() {
        val isFirstRun = getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE)
                .getBoolean("isFirstRun", true)

        if (isFirstRun) {
            startActivity(this, SplashActivity())
        }

        getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE).edit()
                .putBoolean("isFirstRun", false).apply()

    }
    */
}
