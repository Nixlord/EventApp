package com.example.overlord.eventapp.main.wall


import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException

import com.example.overlord.eventapp.R
import com.example.overlord.eventapp.base.BaseFragment
import com.example.overlord.eventapp.extensions.Firebase.firestore
import com.example.overlord.eventapp.extensions.Firebase.storage
import com.example.overlord.eventapp.extensions.inflate
import com.example.overlord.eventapp.extensions.logError
import com.example.overlord.eventapp.extensions.addSnapshotListener
import com.example.overlord.eventapp.extensions.getSimpleName
import com.example.overlord.eventapp.model.Constants
import com.example.overlord.eventapp.model.Post
import com.example.overlord.eventapp.model.User
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_wall.*
import kotlinx.android.synthetic.main.fragment_wall_item.view.*
import java.io.Serializable
import java.lang.Error

class WallFragment : BaseFragment() {

    //Declare your data here
    class FragmentInputs(val postID: String? = null) : Serializable

    interface FragmentInteractor : Serializable {
        //Implement your methods here
        fun onButtonPressed(message: String)
    }

    private var inputs: FragmentInputs? = null
    private var interactor: FragmentInteractor? = null

    companion object {
        @JvmStatic
        fun newInstance(inputs: FragmentInputs?, interactor: FragmentInteractor) =
            WallFragment().apply {
                this.interactor = interactor
                arguments = Bundle().apply { putSerializable("inputs", inputs) }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inputs = arguments?.getSerializable("inputs") as FragmentInputs?
        //Initialize Heavier things here because onCreateView and onViewCreated are called much more number of times
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_wall, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        inputs?.postID?.apply {
//
//            val imageRef = storage.child(Constants.remoteCompressedImages).child(this)
//            Glide.with(base).load(imageRef).into(photoView)
//            base.toastSuccess("HeHeHe")
//
//        } ?: logError(Error("PostID null"))


        val firestoreQuery = firestore.collection("posts").orderBy("date")
        val firestoreOptions = FirestoreRecyclerOptions.Builder<Post>()
            .setLifecycleOwner(this)
            .setQuery(firestoreQuery, Post::class.java)
            .build()


        val firestoreRecyclerAdapter = object : FirestoreRecyclerAdapter<Post, PostHolder>(firestoreOptions) {
            override fun onCreateViewHolder(p0: ViewGroup, p1: Int): PostHolder {
                return PostHolder(
                    p0.inflate(R.layout.fragment_wall_item)
                )
            }

            override fun onBindViewHolder(holder: PostHolder, position: Int, model: Post) {
                holder.bindItems(model)
            }
        }

        wallRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
        wallRecyclerView.adapter = firestoreRecyclerAdapter
    }

    inner class PostHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(post : Post) {
            itemView.apply {

                post.imageID?.apply {

                    val storageReference = storage.child("images").child(this)
                    Glide.with(base).load(storageReference).into(postImageView)

                } ?: logError(Error("No Image ID"))

                firestore.collection("users")
                    .document(post.userID)
                    .addSnapshotListener { snapshot ->
                        val user = snapshot.toObject(User::class.java)
                        if (user != null)
                            postHeader.text = user.name
                        else
                            logError(Error("No User Object"))
                    }
            }
        }
    }
}