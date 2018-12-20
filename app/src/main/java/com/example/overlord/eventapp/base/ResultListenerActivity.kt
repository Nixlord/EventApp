package com.example.overlord.eventapp.base

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import kotlin.random.Random

abstract class ResultListenerActivity : AppCompatActivity() {

    data class ActivityResultAction (
        val action : (() -> Unit),
        val onSuccess : ((Intent) -> Unit),
        val onError : ((Error) -> Unit)
    )

    private val actionRequests : MutableMap<Int, ActivityResultAction> = HashMap()

    private fun addActionRequest(requestCode : Int, action : ActivityResultAction) {
        actionRequests[requestCode] = action
    }

    private fun removeActionRequest(requestCode: Int) {
        actionRequests.remove(requestCode)
    }

    interface actionStep {
        fun perform(action: () -> Unit) : onSuccessStep
    }

    interface onSuccessStep {
        fun onSuccess(onSuccess: (Intent) -> Unit) : onErrorStep
//        fun onResult(onResult: (requestCode : Int, resultCode : Int, data : Intent?) -> Unit)
    }

    interface onErrorStep {
        fun onError(onError: (Error) -> Unit) : finalStep
    }

    interface finalStep {
        fun build()
    }

    inner class ActionStepBuilder(private val requestCode: Int) :
        actionStep,
        onSuccessStep,
        onErrorStep,
        finalStep {
        private lateinit var action : (() -> Unit)
        private lateinit var onSuccess: (Intent) -> Unit
        private lateinit var onError: (Error) -> Unit

        override fun perform(action: () -> Unit): onSuccessStep {
            this.action = action
            return this
        }

        override fun onSuccess(onSuccess: (Intent) -> Unit): onErrorStep {
            this.onSuccess = onSuccess
            return this
        }

        override fun onError(onError: (Error) -> Unit): finalStep {
            this.onError = onError
            return this
        }

        override fun build() {
            addActionRequest(requestCode,
                ActivityResultAction(
                    action,
                    onSuccess,
                    onError
                )
            )
        }
    }

    fun startActivityGetResult(
        activity : Class<*>,
        requestCode : Int = Random.nextInt()
    ) : onSuccessStep {
        return ActionStepBuilder(requestCode).perform {
            startActivityForResult(Intent(this, activity::class.java), requestCode)
        }
    }

    fun startActivityGetResult(
        intent : Intent,
        requestCode : Int = Random.nextInt()
    ) : onSuccessStep {
        return ActionStepBuilder(requestCode).perform {
            startActivityForResult(intent, requestCode)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        for ( (RC, action) in actionRequests ) {
            if (RC == requestCode) {

                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        action.onSuccess(data)
                    }
                    else {
                        action.onError(Error("NULL DATA"))
                    }
                }
                else {
                    action.onError(Error("RESULT CANCELLED"))
                }

                removeActionRequest(RC)
            }

        }
    }
}