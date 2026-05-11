package com.techys.pausa.di

import android.content.Context
import android.content.Intent
import com.techys.core.model.TimerType
import com.techys.core.notification.NotificationActionContract
import com.techys.pausa.MainActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class NotificationContractActionImpl @Inject constructor(@param:ApplicationContext val context: Context): NotificationActionContract {
            override fun getOpenAppIntent(): Intent {
                return Intent(context, MainActivity::class.java)
            }

            override fun getStartFocusTimerIntent(): Intent {
                return Intent(context, MainActivity::class.java).apply {
                    action = TimerType.Focus.id
                }
            }

            override fun getStartQuickTimerIntent(): Intent {
                return Intent(context, MainActivity::class.java).apply {
                    action = TimerType.Quick.id
                }
            }
}