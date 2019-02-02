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
import com.google.android.gms.tasks.Tasks
import com.google.firebase.storage.FileDownloadTask
import com.phoenixoverlord.eventapp.base.BaseActivity
import com.phoenixoverlord.eventapp.extensions.Firebase.firestore
import com.phoenixoverlord.eventapp.extensions.Firebase.storage
import com.phoenixoverlord.eventapp.model.Post
import kotlinx.android.synthetic.main.fragment_photo_item.view.*

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

        val firestoreQuery = firestore.collection("posts")
            .whereArrayContains("tags", albumName!!)

        setupFirestoreRecyclerView(firestoreQuery)

        download_images_fab.setOnClickListener {
            confirmDownloadImages(firestoreQuery)
        }
    }

    private fun confirmDownloadImages(query : Query) {
        val alertDialogBuilder = AlertDialog
                                    .Builder(base)
                                    .setTitle("Download")
                                    .setMessage("Are you sure you want to download all the images of $albumName folder?")
                                    .setCancelable(false)
                                    .setPositiveButton("Yes") { dialog, which ->
                                        base.snackbar("Downloading...")
                                        downloadAllImages(query)
                                    }
                                    .setNegativeButton("No") { dialog, which ->
                                        base.snackbar("You have opted not to download all the images.")
                                        dialog.cancel()
                                    }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    class DownloadTask(private val imageIDs : ArrayList<String>) {



        fun execute(activity : BaseActivity) {

            fun createDownloadTask(index : Int)  : FileDownloadTask {

                val imageID = imageIDs[index]
                val task = storage
                    .child("images/$imageID")
                    .getFile(createImageFile("IMG_$imageID.jpg"))


                activity.notificationModule
                    .createDownloadProgressNotification(
                        12345,
                        "Downloading ${index + 1}/${imageIDs.size}th image",
                        task
                    )

                return task
            }


            val downloadFunctions = imageIDs.mapIndexed { index, _ -> { createDownloadTask(index) } }

            var downloadTask : FileDownloadTask? = null

            downloadFunctions.forEach {function ->
                if (downloadTask == null)
                    downloadTask = function.invoke()
                else {
                    downloadTask?.addOnSuccessListener {
                        downloadTask = function.invoke()
                    }
                }
            }
        }
    }

    private fun downloadAllImages(query: Query) {
        query.get()
            .addOnSuccessListener { querySnapshot ->
                val imageIDs =
                    querySnapshot.documents
                        .mapNotNull { it.toObject(Post::class.java) }
                        .mapNotNull { it.imageID }
                        .toCollection(ArrayList())

                val downloadTask = DownloadTask(imageIDs)
                downloadTask.execute(base)
            }
    }

    private fun setupFirestoreRecyclerView (query : Query) {
        val firestoreOptions = FirestoreRecyclerOptions.Builder<Post>()
            .setLifecycleOwner(this)
            .setQuery(query, Post::class.java)
            .build()

        val firestoreRecyclerAdapter = object : FirestoreRecyclerAdapter<Post, PhotosFragment.PhotosHolder>(firestoreOptions) {
            override fun onCreateViewHolder(p0: ViewGroup, p1: Int): PhotosFragment.PhotosHolder {
                return PhotosHolder(
                    p0.inflate(R.layout.fragment_photo_item)
                )
            }
            override fun onBindViewHolder(holder: PhotosHolder, position: Int, model: Post) {
                holder.bindItems(model)
            }
        }

        photos_recycler_view.layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
        photos_recycler_view.adapter = firestoreRecyclerAdapter
    }

    inner class PhotosHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(post : Post) {
            itemView.apply {
                post.imageID?.apply {
                    base.loadImage(albumImageView, this)
                }
            }
        }
    }
}