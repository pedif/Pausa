package com.techys.settings.util

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import com.techys.core.notification.NotificationManager
import com.techys.settings.model.SoundItem
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TimerSoundManager @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val notificationManager: NotificationManager
) {

    private val prefs = context.getSharedPreferences("timer_settings", MODE_PRIVATE)

    companion object {
        const val KEY_POMODORO_SOUND = "pomodoro_sound"
        const val KEY_QUICK_TIMER_SOUND = "quick_timer_sound"
        const val KEY_ZEN_MODE_SOUND = "zen_mode_sound"

        const val CHANNEL_POMODORO = "pomodoro_timer"
        const val CHANNEL_QUICK_TIMER = "quick_timer"
        const val CHANNEL_ZEN_MODE = "zen_mode"
    }

    // Get default system notification sound
    fun getDefaultSoundUri(): Uri {
        return RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
    }

    // Get all available sounds
    fun getAvailableSounds(): List<SoundItem> {
        val ringtoneManager = RingtoneManager(context)
        ringtoneManager.setType(RingtoneManager.TYPE_NOTIFICATION)

        val sounds = mutableListOf<SoundItem>()
        val cursor = ringtoneManager.cursor

        while (cursor.moveToNext()) {
            val uri = cursor.getString(RingtoneManager.URI_COLUMN_INDEX)
            val title = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX)
            sounds.add(SoundItem(uri, title))
        }

        return sounds
    }

    // Get sound title for display
    fun getSoundTitle(key: String?): String {
        val uri = prefs.getString(key, null) ?: return "Default"

        return try {
            val ringtone = RingtoneManager.getRingtone(context, Uri.parse(uri))
            ringtone?.getTitle(context) ?: "Unknown"
        } catch (e: Exception) {
            "Default"
        }
    }


    // Initialize all channels with saved preferences
    fun initializeChannels() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
            return
        notificationManager.updateChannel(
            CHANNEL_POMODORO,
            "Pomodoro Timer",
            prefs.getString(KEY_POMODORO_SOUND, null)
        )

        notificationManager.updateChannel(
            CHANNEL_QUICK_TIMER,
            "Quick Timer",
            prefs.getString(KEY_QUICK_TIMER_SOUND, null)
        )
        notificationManager.updateChannel(
            CHANNEL_ZEN_MODE,
            "Zen Mode",
            prefs.getString(KEY_ZEN_MODE_SOUND, null)
        )
    }
}

