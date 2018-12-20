package com.example.overlord.eventapp.abstractions;

import android.content.Intent;
import android.support.v4.util.Consumer;

public class ActivityResultStepBuilder {

    public static class ActivityResultAction {
        private Runnable action;
        private Consumer<Intent> onSuccess;
        private Consumer<Error> onError;

        private ActivityResultAction() {}
    }

    public interface internalActionStep {
        internalOnSuccessStep addAction(Runnable action);
    }

    public interface internalOnSuccessStep {
        internalOnErrorStep onSuccess(Consumer<Intent> onSuccess);
    }

    public interface internalOnErrorStep {
        internalFinalStep onError(Consumer<Error> onError);
    }

    public interface internalFinalStep {
        ActivityResultAction build();
    }

    public static class StepBuilder
            implements internalOnSuccessStep, internalOnErrorStep, internalFinalStep {
        ActivityResultAction action;

        StepBuilder() {
            action = new ActivityResultAction();
        }

        @Override
        public internalOnErrorStep onSuccess(Consumer<Intent> onSuccess) {
            action.onSuccess = onSuccess;
            return this;
        }

        @Override
        public internalFinalStep onError(Consumer<Error> onError) {
            action.onError = onError;
            return this;
        }

        @Override
        public ActivityResultAction build() {
            return action;
        }
    }

}
