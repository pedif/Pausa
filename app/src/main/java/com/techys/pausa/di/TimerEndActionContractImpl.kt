package com.techys.pausa.di

import android.content.Context
import android.content.Intent
import com.techys.core.model.TimerType
import com.techys.core.notification.TimerEndActionContract
import com.techys.pausa.end.TimerEndActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TimerEndActionContractImpl @Inject constructor(@param:ApplicationContext val context: Context): TimerEndActionContract {
    override fun getEyeTimerEndAction(): Intent {
        return Intent(context, TimerEndActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_HISTORY
            action = TimerType.EyeBreak.id
        }
    }

    override fun getFocusTimerEndAction(): Intent {
        return Intent(context, TimerEndActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or  Intent.FLAG_ACTIVITY_NO_HISTORY
            action = TimerType.Focus.id
        }
    }

    override fun getQuickTimerEndAction(): Intent {
        return Intent(context, TimerEndActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or  Intent.FLAG_ACTIVITY_NO_HISTORY
            action = TimerType.Quick.id
        }
    }
}