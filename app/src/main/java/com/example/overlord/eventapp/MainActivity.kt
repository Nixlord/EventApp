package com.example.overlord.eventapp

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.overlord.eventapp.model.Guest
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val TAG = "MAIN_ACT"

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

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
/*
    fun loadFragment(fragment: Fragment?) : Boolean {
        fragment?.let {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container)

        }
    }

    fun setupNav(navigationView: BottomNavigationView) {
        navigationView.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.navigation_home ->
            }
        }
    }
*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)


        editTextName.onTextChange { input ->
            textViewInput.text = input
            auth?.currentUser?.email?.let {
                firestore.collection("users")
                        .add(Guest(
                            input,
                            it
                        ))
                    .addOnFailureListener { error -> Log.e(TAG + "onTextChange", error.message ) }
            }
        }

        buttonSignOut.setOnClickListener {
            AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener { finish() }
        }


    }
}
