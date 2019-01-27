package com.example.overlord.eventapp.main.guests

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.overlord.eventapp.R
import java.io.Serializable
import android.view.View
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.example.overlord.eventapp.base.BaseFragment
import com.example.overlord.eventapp.extensions.*
import com.example.overlord.eventapp.main.wall.WallFragment
import com.example.overlord.eventapp.model.Post
import com.example.overlord.eventapp.model.User
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_bride.*
import kotlinx.android.synthetic.main.fragment_guest_item.view.*
import kotlinx.android.synthetic.main.fragment_wall.*
import kotlinx.android.synthetic.main.fragment_wall_item.view.*
import java.lang.Error
import java.util.*

class BrideFragment : BaseFragment() {

    class FragmentInputs(val firstName: String = "Diksha", val surname: String = "Agarwal") : Serializable

    interface FragmentInteractor : Serializable {
        //Implement your methods here
        fun onButtonPressed(message: String)
    }

    private var inputs: BrideFragment.FragmentInputs? = null
    private var interactor: FragmentInteractor? = null

    companion object {
        @JvmStatic
        fun newInstance(inputs: BrideFragment.FragmentInputs?, interactor: FragmentInteractor) =
            BrideFragment().apply {this.interactor = interactor
                arguments = Bundle().apply { putSerializable("inputs", inputs) }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inputs = arguments?.getSerializable("inputs") as BrideFragment.FragmentInputs?
        //Initialize Heavier things here because onCreateView and onViewCreated are called much more number of times
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_bride, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val firestoreQuery = Firebase.firestore.collection("users").whereEqualTo("wedding_side", "Bride")
        setupFirestoreRecyclerView(firestoreQuery)
    }

    private fun setupFirestoreRecyclerView (query : Query) {
        val firestoreOptions = FirestoreRecyclerOptions.Builder<User>()
            .setLifecycleOwner(this)
            .setQuery(query, User::class.java)
            .build()

        val firestoreRecyclerAdapter = object : FirestoreRecyclerAdapter<User, BrideFragment.BrideGuestHolder>(firestoreOptions) {
            override fun onCreateViewHolder(p0: ViewGroup, p1: Int): BrideFragment.BrideGuestHolder {
                return BrideGuestHolder(
                    p0.inflate(R.layout.fragment_guest_item)
                )
            }
            override fun onBindViewHolder(holder: BrideFragment.BrideGuestHolder, position: Int, guest: User) {
                holder.bindItems(guest)
            }
        }
        bride_guest_recycler_view.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
        bride_guest_recycler_view.adapter = firestoreRecyclerAdapter
    }

    inner class BrideGuestHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(guest: User) {
            itemView.apply {

                base.loadImage(guest_profile_image, guest.profile_photo)
                guest_name.text = guest.name
                guest_relation.text = guest.relation
                if(guest.relation == "Bride") {
                    bride_groom_image.visibility = View.VISIBLE
                    Glide.with(this).load(R.drawable.crown).into(bride_groom_image)
                }
                call_button.setOnClickListener {
                    base.withPermissions(
                        Manifest.permission.CALL_PHONE
                    ).execute({
                        startActivity( Intent(Intent.ACTION_CALL).setData(Uri.parse(guest.phoneno)) )
                    }, base::logError)
                }
                message_button.setOnClickListener {
                    startActivity( Intent(Intent.ACTION_VIEW)
                        .setType("vnd.android-dir/mms-sms")
                        .putExtra("Phone Number", guest.phoneno) )
                }

            }
        }
    }
}