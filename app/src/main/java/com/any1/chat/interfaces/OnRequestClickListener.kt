package com.any1.chat.interfaces

import com.any1.chat.models.RequestModel

interface OnRequestClickListener {
    fun onRequestClicked(position : Int , requestList : ArrayList<RequestModel>)
}