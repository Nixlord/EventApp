package com.example.overlord.eventapp.mechanisms

import android.support.v7.app.AppCompatActivity
import com.example.overlord.eventapp.extensions.*
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.DexterError
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

class PermissionsModule {

    private lateinit var dexter: Any

    fun withPermissions(activity: AppCompatActivity, permissions : ArrayList<String>) : PermissionsModule {
        dexter = Dexter.withActivity(activity)
            .withPermissions(permissions)
        return this
    }

    fun execute(
        onGranted : () -> Unit = { logDebug("DefaultPermitCallback", "Default") },
        onError: (Error) -> Unit = { error -> logError("DefaultPermitCallback", error) }
    ) {
        (dexter as Dexter)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (report != null) {
                        if (report.areAllPermissionsGranted()) {
                            onGranted()
                        }
                        else {
                            logError("PermissionsModule", Error("AllPermissionsAreNotGranted"))
                            onError(Error(DexterError.REQUEST_ONGOING.name))
                        }
                    }
                    else {
                        onError(Error("NULL REPORT"))
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }
            })
            .withErrorListener{ error: DexterError? -> onError(Error(error?.name ?: "NullDexterError")) }
            .check()
    }
}