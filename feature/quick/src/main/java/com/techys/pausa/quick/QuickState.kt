package com.techys.pausa.quick

data class QuickState(
    val title: String = "Quick Timer",
    val duration: Int = 10,
    val durationErrorMessage: String = "",
    val titleErrorMessage: String = "",
    val timeList: List<Int> = listOf<Int>(5, 10, 15)
)
