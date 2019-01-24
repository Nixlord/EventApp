package com.example.overlord.eventapp.main.wall


import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout

import com.example.overlord.eventapp.R
import com.example.overlord.eventapp.base.BaseFragment
import com.example.overlord.eventapp.extensions.*
import com.example.overlord.eventapp.extensions.Firebase.auth
import com.example.overlord.eventapp.extensions.Firebase.firestore
import com.example.overlord.eventapp.model.Post
import com.example.overlord.eventapp.model.User
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
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

    fun setupFirestoreRecyclerView(query : Query) {
        val firestoreOptions = FirestoreRecyclerOptions.Builder<Post>()
            .setLifecycleOwner(this)
            .setQuery(query, Post::class.java)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val firestoreQuery = firestore.collection("posts").orderBy("date")
        setupFirestoreRecyclerView(firestoreQuery)
    }

    inner class PostHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(post : Post) {
            itemView.apply {

                val postDocument = firestore.collection("posts").document(post.postID)
                val userDocument = firestore.collection("users").document(post.userID)

                val currentUserID = auth.currentUser?.uid

                postLikeButton.isChecked = post.likedByUIDs.contains(currentUserID)
                postLikeButton.likeCount = post.likedByUIDs.size

                postLikeButton.setOnCheckedChangedListener { button, checked ->
                    val task =
                        if (checked)
                            FieldValue.arrayUnion(currentUserID)
                        else
                            FieldValue.arrayRemove(currentUserID)


                    postDocument.update("likedByUIDs", task)
                        .addOnSuccessListener {
                            button.likeCount = post.likedByUIDs.size
                        }
                        .addOnFailureListener(base::logError)
                }

                post.imageID?.apply {
                    base.loadImage(postImageView, this)
                } ?: logError(Error("No Image ID"))

                userDocument.addSnapshotListener { snapshot ->

                        val user = snapshot.toObject(User::class.java)

                        if (user != null) {
                            //todo These photos appear weird. Have to correct

                            postAuthorView.text = user.name
                            base.loadImage(postAuthorProfileView, user.profile_photo)
                            postCaptionView.text = post.content
                        }

                        else
                            logError(Error("No User Object"))

                }
            }
        }
    }
}