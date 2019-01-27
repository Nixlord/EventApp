package com.example.overlord.eventapp.main.guests

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.example.overlord.eventapp.R
import com.example.overlord.eventapp.base.BaseFragment
import com.example.overlord.eventapp.extensions.*
import com.example.overlord.eventapp.model.User
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_bride.*
import kotlinx.android.synthetic.main.fragment_groom.*
import kotlinx.android.synthetic.main.fragment_guest_item.view.*
import java.io.Serializable
import java.util.ArrayList

class GroomFragment : BaseFragment() {

    class FragmentInputs(val firstName: String = "Diksha", val surname: String = "Agarwal") : Serializable

    interface FragmentInteractor : Serializable {
        //Implement your methods here
        fun onButtonPressed(message: String)
    }

    private var inputs: GroomFragment.FragmentInputs? = null
    private var interactor: FragmentInteractor? = null

    companion object {
        @JvmStatic
        fun newInstance(inputs: GroomFragment.FragmentInputs?, interactor: FragmentInteractor) =
            GroomFragment().apply {this.interactor = interactor
                arguments = Bundle().apply { putSerializable("inputs", inputs) }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inputs = arguments?.getSerializable("inputs") as GroomFragment.FragmentInputs?
        //Initialize Heavier things here because onCreateView and onViewCreated are called much more number of times
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_groom, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val firestoreQuery = Firebase.firestore.collection("users")
            .whereEqualTo("wedding_side", "Groom")
            .whereEqualTo("key_contact", "true")
        setupFirestoreRecyclerView(firestoreQuery)
    }

    private fun setupFirestoreRecyclerView (query : Query) {
        val firestoreOptions = FirestoreRecyclerOptions.Builder<User>()
            .setLifecycleOwner(this)
            .setQuery(query, User::class.java)
            .build()

        val firestoreRecyclerAdapter = object : FirestoreRecyclerAdapter<User, GroomFragment.GroomGuestHolder>(firestoreOptions) {
            override fun onCreateViewHolder(p0: ViewGroup, p1: Int): GroomFragment.GroomGuestHolder {
                return GroomGuestHolder(
                    p0.inflate(R.layout.fragment_guest_item)
                )
            }
            override fun onBindViewHolder(holder: GroomFragment.GroomGuestHolder, position: Int, guest: User) {
                holder.bindItems(guest)
            }
        }
        groom_guest_recycler_view.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
        groom_guest_recycler_view.adapter = firestoreRecyclerAdapter
    }

    inner class GroomGuestHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(guest: User) {
            itemView.apply {

                base.loadImage(guest_profile_image, guest.profile_photo)
                guest_name.text = guest.name
                guest_relation.text = guest.relation
                if(guest.relation == "Groom") {
                    bride_groom_image.visibility = View.VISIBLE
                    Glide.with(this).load(R.drawable.tie).into(bride_groom_image)
                }
                call_button.setOnClickListener {
                    base.withPermissions(
                        Manifest.permission.CALL_PHONE
                    ).execute({
                        val phoneno : String = guest.phoneno
                        val intent = Intent(Intent.ACTION_DIAL).apply {
                            data = Uri.parse("tel:$phoneno")
                        }
                        startActivity(intent)
                    }, base::logError)
                }

                message_button.setOnClickListener {
                    /*startActivity( Intent(Intent.ACTION_VIEW)
                           .setType("vnd.android-dir/mms-sms")
                           .putExtra("Phone Number", guest.phoneno) )*/
                }
            }
        }
    }
}