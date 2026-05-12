package com.techys.pausa.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed class NavRoutes: NavKey {

    @Serializable
    object Home: NavRoutes()
    @Serializable
    object Focus: NavRoutes()
    @Serializable
    object Quick: NavRoutes()

    @Serializable
    object Settings: NavRoutes()

    @Serializable
    object QuickDialog: NavRoutes()
}
