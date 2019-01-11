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
        var onError: ((Error, Intent?) -> Unit)

        init {
            onSuccess = { intent -> logDebug("DefaultARACallback", intent.dataString) }
            onError = { error, intent -> logError("DefaultARACallback", error) }
        }
    }


    private val actionRequests : MutableMap<Int, ActivityResultAction> = ConcurrentHashMap()

    fun createAction(requestCode: Int) = ActionBuilder(requestCode)

    inner class ActionBuilder(private val requestCode: Int) {

        fun perform(runnable: () -> Unit) : ActionBuilder {
            actionRequests[requestCode] = ActivityResultAction()
            // ToDo Sloppy hack. Improve this
            Handler().postDelayed(runnable, 100)
            return this
        }

        fun addOnSuccessListener(onSuccess: (Intent) -> Unit) : ActionBuilder {
            actionRequests[requestCode]?.onSuccess = onSuccess
            return this
        }
        fun addOnFailureListener(onError: (Error, Intent?) -> Unit): ActionBuilder {
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
                        action.onError(Error("NULL DATA"), data)
                    }
                }
                else {
                    action.onError(Error("RESULT CANCELLED"), data)
                }

                actionRequests.remove(RC)
            }
        }
    }
}
