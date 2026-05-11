package com.techys.core.notification

import android.content.Intent

interface TimerEndActionContract {

    fun getEyeTimerEndAction(): Intent
    fun getFocusTimerEndAction(): Intent
    fun getQuickTimerEndAction(): Intent
}