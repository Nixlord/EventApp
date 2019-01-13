package com.example.overlord.eventapp.mechanisms

import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.example.overlord.eventapp.base.BaseActivity
import com.example.overlord.eventapp.extensions.*
import java.util.concurrent.ConcurrentHashMap


class PermissionsModule {

    private class PermissionAction {
        var permissions = arrayListOf<String>()

        var requestPermissions: () -> Unit =
            { logError(Error("Permission Request not made")) }

        var onGranted: () -> Unit =
            {  logDebug("DefaultPermissionCallback", "onGranted") }

        var onError: ((Error) -> Unit) =
            { error -> logError("DefaultARACallback", error) }
    }

    private val permissionRequests : MutableMap<Int, PermissionAction> = ConcurrentHashMap()

    private fun checkPermission(activity: BaseActivity, permission : String) : Boolean {
        return ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED
    }

    inner class PermissionBuilder(private val requestCode : Int) {

        fun withPermissions(activity: BaseActivity, permissions : ArrayList<String>) : PermissionBuilder {

            permissionRequests[requestCode] = PermissionAction().apply {
                this.permissions = permissions
                requestPermissions = { ActivityCompat.requestPermissions(activity, permissions.toTypedArray(), requestCode) }
            }

            return this
        }

        fun execute(
            onGranted : () -> Unit = { logDebug("DefaultPermitCallback", "Default") },
            onError: (Error) -> Unit = { error -> logError("DefaultPermitCallback", error) }
        ) {
            permissionRequests[requestCode].apply {
                this?.onGranted = onGranted
                this?.onError = onError
                this?.requestPermissions?.invoke()
            }
        }
    }

    private fun createMap(permissions: Array<out String>, grantResults: IntArray) : MutableMap<String, Boolean> {
        val map : MutableMap<String, Boolean> = HashMap()
        permissions.mapIndexed { index, permission ->
            map[permission] = grantResults[index] == PackageManager.PERMISSION_GRANTED
        }
        return map
    }

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        val permissionResult = createMap(permissions, grantResults)

        for ((RC, action) in permissionRequests) {
            if (RC == requestCode) {
                val allGranted =
                    action.permissions
                        .map { permission -> permissionResult[permission] ?: false }
                        .reduce { acc, b -> acc && b }
                if (allGranted) {
                    action.onGranted()
                }
                else {
                    logError(Error("All Permissions not granted"))
                    action.permissions
                        .forEach { permission ->
                            if ( ! (permissionResult[permission] ?: false) )
                                logError(Error("$permission not granted"))
                        }
                }
                permissionRequests.remove(RC)
            }
        }
    }
}