package com.techys.settings.model

import android.net.Uri

data class SoundItem(
    val uri: Uri = Uri.EMPTY,
    val title: String = ""
)