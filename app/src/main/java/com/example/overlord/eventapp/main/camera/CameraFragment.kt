package com.example.overlord.eventapp.main.camera

import android.Manifest
import android.os.Bundle
import android.support.design.chip.Chip
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.overlord.eventapp.R
import com.example.overlord.eventapp.base.BaseFragment
import com.example.overlord.eventapp.extensions.*
import com.example.overlord.eventapp.extensions.Firebase.auth
import com.example.overlord.eventapp.extensions.Firebase.firestore
import com.example.overlord.eventapp.extensions.Firebase.storage
import com.example.overlord.eventapp.mechanisms.compressImage
import com.example.overlord.eventapp.model.Post
import com.example.overlord.eventapp.utils.uniqueName
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.fragment_camera.*
import java.io.File
import java.io.Serializable

class CameraFragment : BaseFragment() {

    class FragmentInputs(val firstName : String = "Shibasis", val surname : String = "Patnaik") : Serializable

    interface FragmentInteractor : Serializable {
        fun onImageUploaded(postID : String)
        fun onEditButtonClicked()
    }

    private var inputs: FragmentInputs? = null
    private var interactor: FragmentInteractor? = null

    companion object {
        @JvmStatic
        fun newInstance(inputs : FragmentInputs?, interactor : FragmentInteractor) =
            CameraFragment().apply {
                this.interactor = interactor
                arguments = Bundle().apply { putSerializable("inputs", inputs) }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inputs = arguments?.getSerializable("inputs") as FragmentInputs?
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_camera, container, false)
    }

    private var post = Post()
    private var compressedImage : File? = null
    private var chips : ArrayList<Chip>? = null

    private var selectedTags : MutableMap<String, Boolean> = mutableMapOf(
        Pair("Party", false),
        Pair("Haldi", false),
        Pair("Mehendi", false),
        Pair("Shaadi", false),
        Pair("Reception", false)
    )

    fun setupChipLayout(){

        chips = arrayListOf(
            chipHaldi,
            chipMehendi,
            chipParty,
            chipShaadi,
            chipReception
        )

        chips!!.map { chip ->
            chip.setOnCheckedChangeListener { _, isChecked ->
                selectedTags[chip.text.toString()] = isChecked
                logDebug("CurrentlySelected: ", selectedTags.toString())
            }
        }
    }

    fun getSelectedChips() = selectedTags.filter { it.value }.keys.toCollection(ArrayList())


    fun setupCamera() {
        photoView.setOnClickListener {
            base.apply {
                withPermissions(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                ).execute ({
                    takePhoto("Upload Photos")
                        .addOnSuccessListener { image ->

                            logDebug("Cache File ${image.name}")

                            val imageName = uniqueName()

                            compressedImage = compressImage(image, imageName)
                            post.imageID = imageName

                            loadImage(photoView, image)
                        }
                }, this::logError)
            }
        }
    }

    fun setupEditText() {
        contentView.onTextChange { content -> post.content = content }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupChipLayout()
        setupCamera()
        setupEditText()

        submitButton.setOnClickListener {

            compressedImage?.apply {

                post.userID = auth.uid ?: "NULL UID"
                post.postID = uniqueName()
                post.tags = getSelectedChips()

                firestore.savePost(post, this) {
                    val success = "Uploaded : ${post.postID}"
                    logDebug(success)
                    base.toastSuccess(success)
                    resetViews()
                }
            }
        }
    }

    private fun resetViews() {
        val postID = post.postID

        contentView.setText("")
        photoView.setImageResource(R.drawable.female)

        chips!!.forEach { chip -> chip.isChecked = false }

        post = Post()
        compressedImage = null

        interactor?.onImageUploaded(postID)
    }
}
