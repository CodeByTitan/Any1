package com.any1.chat.models

import android.net.Uri

data class MemberModel(val membername : String, val memberusername : String, val uri : String, val connected : Boolean, val isAdmin : Boolean)