package com.techys.pausa.navigation

sealed class Route {

    object Home: Route()
    object Focus: Route()
    object Quick: Route()
}
