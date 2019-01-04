package com.example.overlord.eventapp.abstractions

import android.app.Activity
import android.content.Intent
import android.os.Handler
import com.example.overlord.eventapp.extensions.logDebug
import com.example.overlord.eventapp.extensions.logError
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

class ActivityResultAction {
    var onSuccess: ((Intent) -> Unit) = { intent -> logDebug("DefaultCallback", intent.dataString) }
    var onError: ((Error) -> Unit)

    init {
        onSuccess = { intent -> logDebug("DefaultCallback", intent.toString()) }
        onError = { error -> logError("DefaultCallback", error.message) }
    }
}

class ActivityResultHandler {

    private val actionRequests : MutableMap<Int, ActivityResultAction> = ConcurrentHashMap()

    private val startSeed = 100
    private val endSeed = 999 // 16bit => 65535 upper limit
    private var atomicInteger = AtomicInteger(startSeed)

    fun nextRequestCode() : Int {
        atomicInteger.compareAndSet(endSeed, startSeed)
        return atomicInteger.incrementAndGet()
    }

    inner class ActionBuilder(private val requestCode: Int) {

        fun perform(runnable: () -> Unit) : ActionBuilder {
            actionRequests[requestCode] = ActivityResultAction()
            Handler().postDelayed(runnable, 100)
            return this
        }

        fun onSuccess(onSuccess: (Intent) -> Unit) : ActionBuilder {
            actionRequests[requestCode]?.onSuccess = onSuccess
            return this
        }

        fun onError(onError: (Error) -> Unit): ActionBuilder {
            actionRequests[requestCode]?.onError = onError
            return this
        }
    }

    fun createAction(requestCode: Int) = ActionBuilder(requestCode)

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
