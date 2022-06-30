package com.any1.chat.models

import android.net.Uri

data class RequestModel(val name : String, val username : String, val uri: String, var isChecked : Boolean, val id : String)