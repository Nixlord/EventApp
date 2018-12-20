package com.example.overlord.eventapp.base

import android.os.Bundle
import android.os.PersistableBundle
import icepick.Icepick

abstract class StatefulActivity : ResultListenerActivity() {
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

        // You can put the calls to Icepick into a StatefulActivity
        // All Activities extending StatefulActivity automatically have state saved/restored
    }
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Icepick.restoreInstanceState(this, savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        super.onSaveInstanceState(outState, outPersistentState)
        Icepick.saveInstanceState(this, outState)

    }
}