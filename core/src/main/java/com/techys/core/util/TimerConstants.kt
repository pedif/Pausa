package com.techys.core.util

object TimerConstants {

    /**
     * The Ids for the default timers
     */
    const val EYE_TIMER_ID = 1
    const val FOCUS_TIMER_ID = 2
    const val QUICK_TIMER_ID = 3


    /**
     * Default intervals for the timers in seconds
     */
    const val DEFAULT_EYE_INTERVAL = 20 * 60
    const val DEFAULT_FOCUS_INTERVAL = 60 * 60
    const val DEFAULT_QUICK_INTERVAL = 30 * 60

    const val DEFAULT_EYE_COOLDOWN_INTERVAL = 20

    /**
     * We will use this id to display the end of the eye timer event to the user
     */
    const val EYE_TIMER_END_ID = 10001

    const val QUICK_TIMER_MAX_INTERVAL = 120 * 60
    const val FOCUS_TIMER_MAX_INTERVAL = 120 * 60


}