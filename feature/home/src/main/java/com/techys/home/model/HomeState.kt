package com.techys.home.model

data class HomeState(
    val isEyeTimerHelperEnabled: Boolean = true,
    val isFocusTimerHelperEnabled: Boolean = true,
    val isQuickTimerHelperEnabled: Boolean = true
)
