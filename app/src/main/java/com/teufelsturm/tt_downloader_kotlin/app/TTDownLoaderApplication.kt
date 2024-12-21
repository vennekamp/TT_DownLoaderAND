package com.teufelsturm.tt_downloader_kotlin.app

import android.util.Log
import androidx.multidex.MultiDexApplication
import dagger.hilt.android.HiltAndroidApp
// Constant tag used for logging purposes, typically to identify which class the log message is coming from
private const val TAG = "TTDownLoaderApplication"

// @HiltAndroidApp annotation triggers Hilt's code generation for this application class,
// allowing Hilt to inject dependencies into this application and throughout the app.
@HiltAndroidApp
class TTDownLoaderApplication : MultiDexApplication() {

    // The onCreate() method is called when the application is first created (before any activities, services, or receivers are created)
    override fun onCreate() {
        super.onCreate()

        // Logs a verbose message indicating that the onCreate() method of this application class has been called.
        // This is useful for debugging and verifying the app lifecycle.
        Log.v(TAG, "onCreate()")
    }
}
