package com.any1.chat.models

import android.net.Uri

data class GroupModel(val item: String, var uri: String, val tag : String, var isMuted: Boolean ) {
    init {
        isMuted = false
    }
}