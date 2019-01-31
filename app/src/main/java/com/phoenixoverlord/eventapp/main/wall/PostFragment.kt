package com.phoenixoverlord.eventapp.main.wall


import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout

import com.phoenixoverlord.eventapp.R
import com.phoenixoverlord.eventapp.base.BaseFragment
import com.phoenixoverlord.eventapp.extensions.*
import com.phoenixoverlord.eventapp.extensions.Firebase.auth
import com.phoenixoverlord.eventapp.extensions.Firebase.firestore
import com.phoenixoverlord.eventapp.model.Comment
import com.phoenixoverlord.eventapp.model.Post
import com.phoenixoverlord.eventapp.model.User
import com.phoenixoverlord.eventapp.utils.uniqueName
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.comments_view_holder.view.*
import kotlinx.android.synthetic.main.fragment_post.*
import java.lang.Error
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class PostFragment : BaseFragment() {

    private var postID : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postID = arguments?.getString("postID")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post, container, false)
    }

    inner class CommentHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(comment : Comment) {
            itemView.apply {
                commentTextView.text = comment.content

                val dateFormat = SimpleDateFormat("dd MMM',' h a", Locale.UK)
                commentTimeTextView.text = dateFormat.format(comment.date)

                val userDocument = firestore.document("users/${comment.userID}")
                userDocument.addSnapshotListener(User::class.java) { user ->
                    base.loadCircularImage(commentUserProfilePhoto, user.profile_photo)
                    commentUserNameView.text = user.name
                }

                val userID = auth.currentUser?.uid.toString()
                val commenterID = comment.userID

                if (userID == commenterID)
                    setOnLongClickListener {
                        AlertDialog.Builder(base)
                            .setMessage("Do you want to delete this post?")
                            .setPositiveButton("Delete") { _, _ ->
                                firestore.document("posts/$postID/comments/${comment.commentID}").delete()
                                    .addOnFailureListener { error -> logError(error) }

                            }
                            .setCancelable(true)
                            .show()

                        true
                    }
            }
        }
    }

    fun setupRecyclerView(query: Query) {
        val options = FirestoreRecyclerOptions.Builder<Comment>()
            .setLifecycleOwner(this)
            .setQuery(query, Comment::class.java)
            .build()

        val adapter = object :  FirestoreRecyclerAdapter<Comment, CommentHolder>(options) {
            override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CommentHolder {
                return CommentHolder(
                    p0.inflate(R.layout.comments_view_holder)
                )
            }

            override fun onBindViewHolder(holder: CommentHolder, position: Int, model: Comment) {
                holder.bindItems(model)
            }
        }

        commentsRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
        commentsRecyclerView.adapter = adapter
    }

    var commentInput = ""
    fun setupViews(postID: String) {

        val postDocument = firestore.document("posts/$postID")
        postDocument.addSnapshotListener(Post::class.java) {
            it.imageID?.apply {
                base.loadImage(postPhoto, this)
            }
            postContentView.text = it.content
        }

        val commentsCollection = firestore.collection("posts/$postID/comments")

        commentInputView.onTextChange {
            commentInput = it
        }

        commentSubmitButton.setOnClickListener {
            val comment = Comment(
                commentID = uniqueName(),
                userID = auth.currentUser?.uid.toString(),
                content = commentInput
            )
            commentsCollection.document(comment.commentID)
                .set(comment)
                .addOnFailureListener { error -> logError(error) }

            commentInputView.setText("")
            commentInput = ""
        }

        val commentsQuery = commentsCollection.orderBy("date", Query.Direction.DESCENDING)
        setupRecyclerView(commentsQuery)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postID?.apply(::setupViews) ?: logError(Error("POST ID NULL"))
    }

    companion object {
        @JvmStatic
        fun newInstance(postID : String) =
            PostFragment().apply {
                arguments = Bundle().apply { putString("postID", postID) }
            }
    }
}
