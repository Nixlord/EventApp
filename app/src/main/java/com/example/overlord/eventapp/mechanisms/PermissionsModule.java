package com.example.overlord.eventapp.mechanisms;

import android.app.Activity;
import android.support.v4.util.Consumer;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.overlord.eventapp.extensions.ActivityUtils.logError;

public class PermissionsModule {
    private ArrayList<String> permissions;
    private Runnable onSuccessCallback;
    private Consumer<DexterError> onErrorCallback;

    public static internalPermissionsStep withActivity(Activity activity) {
        return new StepBuilder(activity);
    }

    public interface internalPermissionsStep {
        internalOnSuccessStep requestPermissions(ArrayList<String> permissions);
    }

    public interface internalOnSuccessStep {
        internalOnErrorStep onSuccess(Runnable onSuccessCallback);
    }

    public interface internalOnErrorStep {
        internalFinalStep onError(Consumer<DexterError> onErrorCallback);
    }

    public interface internalFinalStep {
        void build();
    }
  
    public static class StepBuilder
            implements internalPermissionsStep, internalOnSuccessStep, internalOnErrorStep, internalFinalStep {

        Activity activity;
        PermissionsModule permissionsModule;

        private StepBuilder(Activity activity) {
            this.activity = activity;
            permissionsModule = new PermissionsModule();
        }

        @Override
        public internalOnSuccessStep requestPermissions(ArrayList<String> permissions) {
            permissionsModule.permissions = permissions;
            return this;
        }

        @Override
        public internalOnErrorStep onSuccess(Runnable onSuccessCallback) {
            permissionsModule.onSuccessCallback = onSuccessCallback;
            return this;
        }

        @Override
        public internalFinalStep onError(Consumer<DexterError> onErrorCallback) {
            permissionsModule.onErrorCallback = onErrorCallback;
            return this;
        }

        @Override
        public void build() {
            Dexter.withActivity(activity)
                    .withPermissions(permissionsModule.permissions)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report != null) {
                                if (report.areAllPermissionsGranted()) {
                                    permissionsModule.onSuccessCallback.run();
                                }
                                else {
                                    logError("PermissionsModule", "AllPermissionsAreNotGranted");
                                    permissionsModule.onErrorCallback.accept(DexterError.REQUEST_ONGOING);
                                }
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            if (token != null) {
                                token.continuePermissionRequest();
                            }
                        }
                    })
                    .withErrorListener(permissionsModule.onErrorCallback::accept)
                    .check();

            activity = null;
        }
    }

}
