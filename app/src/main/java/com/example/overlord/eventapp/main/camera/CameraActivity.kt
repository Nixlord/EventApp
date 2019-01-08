package com.example.overlord.eventapp.main.camera

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.overlord.eventapp.R
import com.google.firebase.storage.FirebaseStorage
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.activity_camera.*

import pl.aprilapps.easyphotopicker.EasyImage
import java.io.File
import java.io.FileInputStream
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*


fun Activity.snackbar(message : String) {
    Snackbar.make(this.findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show()
}

fun timeStamp() = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())

class CameraActivity : AppCompatActivity() {

    val storageRef = FirebaseStorage.getInstance().reference
    val imagesRef = storageRef.child("images")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
//        setSupportActionBar(toolbar)


        FloatingActionButton(this)
            .setOnClickListener {
            Dexter.withActivity(this)
                .withPermissions(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                )
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                        report?.let {
                            if (report.areAllPermissionsGranted()) {
                                snackbar("All granted")
                                EasyImage.openChooserWithDocuments(
                                    this@CameraActivity,
                                    "Upload Photo",
                                    1
                                )

                            }
                            else
                                snackbar("You have to grant all permissions")
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: MutableList<PermissionRequest>?,
                        token: PermissionToken?
                    ) {
                        token?.continuePermissionRequest()
                    }
                })
                .withErrorListener { error ->  Log.e("MainAct", error.toString()) }
                .check()
            }
    }

    fun loadImage(imageView: ImageView, file : Any) {
        Glide.with(this)
            .load(file)
            .into(imageView)
    }

    fun ImageView.load(file: Any) {

        Glide.with(this@CameraActivity)
            .load(file)
            .into(this)
    }


    /*

    withPermissions()
    doThis()
    addOnFailureListener()

    startActivityForResult()
    execute()

     */

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, object : EasyImage.Callbacks {

            override fun onImagesPicked(p0: MutableList<File>, p1: EasyImage.ImageSource?, p2: Int) {

                p0.forEach { Log.d("MainAct:Result", it.toString()) }

                if (p0.size != 0) {
                    val image = p0[0]
                    val compressedImage = Compressor(this@CameraActivity).compressToFile(image)

                    imagesRef.child(timeStamp()).putStream(FileInputStream(compressedImage))
                        .addOnSuccessListener { imageView.load(compressedImage) }
                        .addOnFailureListener { snackbar(it.message.toString()) }
                }

            }

            override fun onImagePickerError(p0: Exception?, p1: EasyImage.ImageSource?, p2: Int) {
                Log.e("MainAct:Result", p0.toString())
            }

            override fun onCanceled(p0: EasyImage.ImageSource?, p1: Int) {
                snackbar("Photo Upload Cancelled")
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        EasyImage.clearConfiguration(this)
    }
}
