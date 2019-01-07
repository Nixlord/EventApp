package com.example.overlord.eventapp.mechanisms

import android.app.Activity
import android.content.Intent
import android.os.Handler
import com.example.overlord.eventapp.extensions.logDebug
import com.example.overlord.eventapp.extensions.logError
import java.util.concurrent.ConcurrentHashMap


class ActivityResultHandler {

    private class ActivityResultAction {
        var onSuccess: ((Intent) -> Unit)
        var onError: ((Error) -> Unit)

        init {
            onSuccess = { intent -> logDebug("DefaultARACallback", intent.dataString) }
            onError = { error -> logError("DefaultARACallback", error.message) }
        }
    }


    private val actionRequests : MutableMap<Int, ActivityResultAction> = ConcurrentHashMap()

    fun createAction(requestCode: Int) = ActionBuilder(requestCode)

    inner class ActionBuilder(private val requestCode: Int) {

        fun perform(runnable: () -> Unit) : ActionBuilder {
            actionRequests[requestCode] = ActivityResultAction()
            Handler().postDelayed(runnable, 100)
            return this
        }

        fun addOnSuccessListener(onSuccess: (Intent) -> Unit) : ActionBuilder {
            actionRequests[requestCode]?.onSuccess = onSuccess
            return this
        }
        fun addOnFailureListener(onError: (Error) -> Unit): ActionBuilder {
            actionRequests[requestCode]?.onError = onError
            return this
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

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

                actionRequests.remove(RC)
            }
        }
    }
}
