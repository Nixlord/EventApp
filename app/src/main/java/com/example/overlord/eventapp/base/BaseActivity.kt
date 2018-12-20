package com.example.overlord.eventapp.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import com.example.overlord.eventapp.extensions.logDebug
import com.example.overlord.eventapp.extensions.logError
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import icepick.Icepick
import pl.aprilapps.easyphotopicker.EasyImage
import pl.aprilapps.easyphotopicker.EasyImageConfiguration

abstract class BaseActivity : AppCompatActivity() {
    /*
    IcePick Usage
    class ExampleActivity extends Activity {
  @State String username; // This will be automatically saved and restored

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Icepick.restoreInstanceState(this, savedInstanceState);
  }

  @Override public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    Icepick.saveInstanceState(this, outState);
  }

  // You can put the calls to Icepick into a BaseActivity
  // All Activities extending BaseActivity automatically have state saved/restored
}
     */

    /***/
    // Not putting in Singleton because firestore has a reference to context.
    // Keeping a reference will prevent activity from getting garbage collected
    val firebaseAuth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()
    val firebaseStorage = FirebaseStorage.getInstance()


    /** Extract this portion after its functional */

    val actionRequests : MutableMap<Int, ActivityResultAction> = HashMap()

    fun addActionRequest(requestCode : Int, action : ActivityResultAction) {
        actionRequests[requestCode] = action
    }

    fun removeActionRequest(requestCode: Int) {
        actionRequests.remove(requestCode)
    }

    class ActivityResultAction (
        var action : (() -> Unit),
        var onSuccess : ((Intent) -> Unit),
        var onError : ((Error) -> Unit)
    )

    interface actionStep {
        fun perform(action: () -> Unit) : onSuccessStep
    }

    interface onSuccessStep {
        fun onSuccess(onSuccess: (Intent) -> Unit) : onErrorStep
    }

    interface onErrorStep {
        fun onError(onError: (Error) -> Unit) : finalStep
    }

    interface finalStep {
        fun build()
    }

    inner class ActionStepBuilder(private val requestCode: Int) : actionStep, onSuccessStep, onErrorStep, finalStep {
        private val activityResultAction = ActivityResultAction()

        override fun perform(action: () -> Unit): onSuccessStep {
            activityResultAction.action = action
            return this
        }

        override fun onSuccess(onSuccess: (Intent) -> Unit): onErrorStep {
            activityResultAction.onSuccess = onSuccess
            return this
        }

        override fun onError(onError: (Error) -> Unit): finalStep {
            activityResultAction.onError = onError
            return this
        }

        override fun build() {
            addActionRequest(requestCode, activityResultAction)
        }
    }


    fun startActivityGetResult(requestCode : Int = 1, activity : Class<*>) = ActionStepBuilder(requestCode).perform {
        startActivityForResult(Intent(this, activity::class.java), requestCode)
    }


    fun takePhoto(requestCode: Int = 2, chooserTitle : String = "Upload Photo") = ActionStepBuilder(requestCode).perform {
        EasyImage.openChooserWithDocuments(
            this,
            chooserTitle,
            0
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Icepick.restoreInstanceState(this, savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        super.onSaveInstanceState(outState, outPersistentState)
        Icepick.saveInstanceState(this, outState)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        for ( (RC, action) in actionRequests ) {
            if (RC == requestCode) {
                val success = resultCode == Activity.RESULT_OK && data != null
                if (success) {
                    action.onSuccess(data!!)
                }
                else {
                    action.onError(Error("Cancelled or Null Intent"))
                }
                actionRequests.remove(RC)
            }

        }
    }

    /*

    cameraModule
    .takePhoto()
    .onSuccess()
    .onError()
    .build()

    startActivityWithResultListener(MainActivity)
    .onResult(data : Intent -> {

    })
     */

}