package com.any1.chat.models

import android.net.Uri

data class SearchModel (val item : String, val uri: String, val isApprovalRequired : Boolean, val membercount : Long, val tag : String, val memberList: ArrayList<String>, val requestList : ArrayList<String>, val connections : Int){
}