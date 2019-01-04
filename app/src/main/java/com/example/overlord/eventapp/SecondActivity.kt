package com.example.overlord.eventapp

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        setResult(Activity.RESULT_OK, Intent().putExtra("Name", "SHIBASIS PATNAIK"))
        finish()
    }
}
