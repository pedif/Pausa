package com.techys.pausa

import android.app.Application
import com.techys.core.util.AppConstants
import dagger.hilt.android.HiltAndroidApp
//TODO V2 add ending sound for eye care cooldown
//TODO V2 add whether the user wants to hear the ringtone or not for timers each individually
@HiltAndroidApp
class App: Application() {
    override fun onCreate() {
        super.onCreate()
        AppConstants.versionName = BuildConfig.VERSION_NAME
    }
}