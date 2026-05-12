package com.techys.pausa.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class NavRoutes {

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
