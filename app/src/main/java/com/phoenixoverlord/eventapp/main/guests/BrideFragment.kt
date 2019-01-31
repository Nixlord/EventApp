package com.phoenixoverlord.eventapp.main.guests

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.phoenixoverlord.eventapp.R
import android.view.View
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.phoenixoverlord.eventapp.base.BaseFragment
import com.phoenixoverlord.eventapp.extensions.*
import com.phoenixoverlord.eventapp.model.User
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_bride.*
import kotlinx.android.synthetic.main.fragment_guest_item.view.*

class BrideFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_bride, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val firestoreQuery = Firebase.firestore.collection("users")
            .whereEqualTo("wedding_side", "Bride")
            .whereEqualTo("key_contact", "true")
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
                    ).execute {
                        /*
                        val phoneno : String = guest.phoneno
                        val intent = Intent(Intent.ACTION_DIAL).apply {
                            data = Uri.parse("tel:$phoneno")
                        }
                        startActivity(intent)
                        */
                        base.safeIntentDispatch(
                            Intent(Intent.ACTION_DIAL).apply {
                                data = Uri.parse("tel:${guest.phoneno}")
                            }
                        )

                    }
                }

                message_button.setOnClickListener {

                    base.apply {

                        val whatsAppIntent = Intent(Intent.ACTION_VIEW).apply {
                            data = Uri.parse("https://api.whatsapp.com/send?phone=${guest.phoneno}")
                            `package` = "com.whatsapp"
                        }

                        val textIntent = Intent(Intent.ACTION_SEND).apply {
                            data = Uri.parse("smsto:${guest.phoneno}")
                            type = "text/plain"
                        }

                        if (packageManager.intentHandlerExists(whatsAppIntent))
                            safeIntentDispatch(whatsAppIntent)
                        else
                            safeIntentDispatch(textIntent)

                    }
                }

            }
        }
    }
}