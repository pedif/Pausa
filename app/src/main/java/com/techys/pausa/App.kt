package com.techys.pausa

import android.app.Application
import com.techys.core.util.AppConstants
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App: Application() {
    override fun onCreate() {
        super.onCreate()
        AppConstants.versionName = BuildConfig.VERSION_NAME
    }
}