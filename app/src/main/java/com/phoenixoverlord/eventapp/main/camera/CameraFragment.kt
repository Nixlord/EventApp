package com.phoenixoverlord.eventapp.main.camera

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.support.design.chip.Chip
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.tasks.Tasks
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.UploadTask
import com.phoenixoverlord.eventapp.R
import com.phoenixoverlord.eventapp.base.BaseActivity
import com.phoenixoverlord.eventapp.base.BaseFragment
import com.phoenixoverlord.eventapp.extensions.*
import com.phoenixoverlord.eventapp.extensions.Firebase.auth
import com.phoenixoverlord.eventapp.extensions.Firebase.firestore
import com.phoenixoverlord.eventapp.mechanisms.compressImage
import com.phoenixoverlord.eventapp.mechanisms.filters.FilterActivity
import com.phoenixoverlord.eventapp.model.Post
import com.phoenixoverlord.eventapp.utils.uniqueName
import kotlinx.android.synthetic.main.fragment_camera.*
import java.io.File
import java.io.Serializable
import java.util.*

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
        Pair("Sangeet", false),
        Pair("Wedding", false),
        Pair("Reception", false)
    )

    fun setupChipLayout(){

        chips = arrayListOf(
            chipHaldi,
            chipMehendi,
            chipParty,
            chipSangeet,
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

    class ImageUploadTask(private val imageFiles : MutableList<File>) {
        fun execute(activity : BaseActivity) {
            fun createUploadTask(index : Int)  : UploadTask {
                val file = imageFiles[index]

                val post = Post(
                    userID = auth.uid!!,
                    postID = uniqueName(),
                    imageID = uniqueName(),
                    tags = arrayListOf("Wedding"),
                    content = "Snigdha Charan Marriage"
                )
                val task =  firestore.savePost(post, file)

                activity.notificationModule
                    .createUploadProgressNotification(
                        54321,
                        "Uploading ${index + 1}/${imageFiles.size}th image",
                        task
                    )

                return task
            }

            val uploadFunctions = imageFiles.mapIndexed { index, _ -> { createUploadTask(index) } }

            var uploadTask : UploadTask? = null

            uploadFunctions.forEach {function ->
                if (uploadTask == null)
                    uploadTask = function.invoke()
                else {
                    uploadTask?.addOnSuccessListener {
                        uploadTask = function.invoke()
                    }
                }
            }
        }
    }

    fun setupCamera() {
        photoView.setOnClickListener {
            base.apply {
                withPermissions(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                ).execute {
                    takePhoto("Upload Photos")
                        .addOnSuccessListener { images ->

                            if (images.size == 1) {
                                val image = images[0]

                                logDebug("Cache File ${image.name}")

                                val imageName = uniqueName()

                                compressedImage = compressImage(image, imageName)
                                post.imageID = imageName

                                loadImage(photoView, image)
                            }
                            else if (images.size > 1) {
                                val uploadTask = ImageUploadTask(images)
                                uploadTask.execute(base)
                                resetViews()
                            }
                        }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupChipLayout()
        setupCamera()

        submitButton.setOnClickListener {

            compressedImage?.apply {

                post.userID = auth.uid ?: "NULL UID"
                post.postID = uniqueName()
                post.tags = getSelectedChips()
                post.content = contentView.text.toString()

                val uploadTask = firestore.savePost(post, this) {
                    //ToDo create notification here to signal completion

                    val postID = post.postID
                    val success = "Uploaded : $postID"
                    logDebug(success)
                    base.toastSuccess(success)
                }

                base.notificationModule.createUploadProgressNotification(
                    123,
                    "Photo Upload",
                    uploadTask
                )

                resetViews()
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
