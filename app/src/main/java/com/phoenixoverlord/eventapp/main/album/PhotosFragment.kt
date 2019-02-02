package com.phoenixoverlord.eventapp.main.album

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query
import com.phoenixoverlord.eventapp.R
import com.phoenixoverlord.eventapp.base.BaseFragment
import com.phoenixoverlord.eventapp.extensions.*
import com.phoenixoverlord.eventapp.model.User
import kotlinx.android.synthetic.main.fragment_photos.*
import android.support.v7.app.AlertDialog

class PhotosFragment : BaseFragment() {

    private var albumName : String? = null

    companion object {
        @JvmStatic
        fun newInstance(tag : String) =
            PhotosFragment().apply {
                arguments = Bundle().apply { putString("albumName", tag) }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        albumName = arguments?.getString("albumName")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_photos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val firestoreQuery = Firebase.firestore.collection("posts")
            .whereArrayContains("tags", albumName!!)

        setupFirestoreRecyclerView(firestoreQuery)

        download_images_fab.setOnClickListener {
            confirmDownloadImages()
        }
    }

    private fun confirmDownloadImages() {
        val alertDialogBuilder = AlertDialog
                                    .Builder(context!!)
                                    .setTitle("Download")
                                    .setMessage("Are you sure you want to download all the images of $albumName folder?")
                                    .setCancelable(false)
                                    .setPositiveButton("Yes") { dialog, which ->
                                        base.snackbar("Downloading...")
                                        downloadAllImages()
                                    }
                                    .setNegativeButton("No") { dialog, which ->
                                        base.snackbar("You have opted not to download all the images.")
                                        dialog.cancel()
                                    }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }    

    private fun downloadAllImages() {

    }

    private fun setupFirestoreRecyclerView (query : Query) {
        val firestoreOptions = FirestoreRecyclerOptions.Builder<User>()
            .setLifecycleOwner(this)
            .setQuery(query, User::class.java)
            .build()

        val firestoreRecyclerAdapter = object : FirestoreRecyclerAdapter<User, PhotosFragment.PhotosHolder>(firestoreOptions) {
            override fun onCreateViewHolder(p0: ViewGroup, p1: Int): PhotosFragment.PhotosHolder {
                return PhotosHolder(
                    p0.inflate(R.layout.fragment_photo_item)
                )
            }
            override fun onBindViewHolder(holder: PhotosFragment.PhotosHolder, position: Int) {

            }
        }

        photos_recycler_view.layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
        photos_recycler_view.adapter = firestoreRecyclerAdapter
    }

    inner class PhotosHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems() {
            itemView.apply {

            }
        }
    }
}