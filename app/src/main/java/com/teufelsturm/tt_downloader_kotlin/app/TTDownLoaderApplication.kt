package com.teufelsturm.tt_downloader_kotlin.app

import android.util.Log
import androidx.multidex.MultiDexApplication
import dagger.hilt.android.HiltAndroidApp

private const val TAG = "TTDownLoaderApplication"

@HiltAndroidApp
class TTDownLoaderApplication : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        Log.v(TAG, "onCreate()")
    }
}