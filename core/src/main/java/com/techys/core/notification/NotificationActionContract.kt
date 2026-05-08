package com.techys.core.notification

import android.content.Intent

interface NotificationActionContract {

    fun getOpenAppIntent(): Intent
    fun getStartFocusTimerIntent(): Intent
    fun getStartQuickTimerIntent(): Intent

}