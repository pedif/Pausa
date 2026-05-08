package com.techys.settings.model

/**
 * Manifest permissions in viewmodel state or directly in ui usage?
 * what of permission requests?
 * shall we put feature settins here or on their home cards ?
 * what should tapping on feature card do on home screen
 * free ai to help me with the ui/ux?
 * thinking a block of 2x2 grid with the first 3 features and the last the gear icon for settings
 * or having full width grid items for features
 *
 * while notif channel is not a permission, the user might disable it, shall we includeit inside permissions box?
 */
data class SettingsState(
    val hasPermission: Boolean = false
)
