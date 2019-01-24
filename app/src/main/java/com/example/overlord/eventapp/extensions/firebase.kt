package com.example.overlord.eventapp.extensions

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.overlord.eventapp.base.BaseActivity
import com.example.overlord.eventapp.extensions.Firebase.auth
import com.example.overlord.eventapp.extensions.Firebase.firestore
import com.example.overlord.eventapp.extensions.Firebase.storage
import com.example.overlord.eventapp.model.Constants
import com.example.overlord.eventapp.model.Post
import com.example.overlord.eventapp.utils.uniqueName
import com.github.chrisbanes.photoview.PhotoView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.File
import java.io.FileInputStream
import java.lang.Error


object Firebase {
    /**
     * Custom getters so that we don't store a reference to context
     * (FirebaseFirestore.getInstance() has reference to context)
     */
    val storage : StorageReference
        get() = FirebaseStorage.getInstance().reference

    val firestore : FirebaseFirestore
        get() = FirebaseFirestore.getInstance()

    val auth : FirebaseAuth
        get() = FirebaseAuth.getInstance()
}


fun StorageReference.pushImage(compressedImage : File, imageName: String) : UploadTask {
    val imageStorage = this.child("images").child(imageName)

    return imageStorage.putStream(
            FileInputStream(
                compressedImage
            )
        )
}


fun FirebaseFirestore.savePost(post: Post, image: File, onSuccess : (task: UploadTask.TaskSnapshot) -> Unit) {

    val postDocument = this.collection("posts").document(post.postID)

    postDocument.set(post)
        .addOnSuccessListener {

            post.imageID?.apply {

                storage.pushImage(image, this)
                    .addOnSuccessListener(onSuccess)
                    .addOnFailureListener(::logError)

            } ?: logError(Error("Null Image ID"))

        }
        .addOnFailureListener(::logError)
}

fun DocumentReference.addSnapshotListener(
    snapshotListener : (snapshot : DocumentSnapshot) -> Unit
) {
    this.addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
        if (firebaseFirestoreException != null) {
            logError(firebaseFirestoreException)
        }

        else if (documentSnapshot == null || ! documentSnapshot.exists()) {
            logError(Error("No Such Document"))
        }

        else
            snapshotListener.invoke(documentSnapshot)

    }
}