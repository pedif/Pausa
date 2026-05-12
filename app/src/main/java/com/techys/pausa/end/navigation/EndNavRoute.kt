package com.techys.pausa.end.navigation

import kotlinx.serialization.Serializable

sealed class EndNavRoute {
    @Serializable
    object Eye: EndNavRoute()
    @Serializable
    object Focus: EndNavRoute()
    @Serializable
    object Quick: EndNavRoute()
}