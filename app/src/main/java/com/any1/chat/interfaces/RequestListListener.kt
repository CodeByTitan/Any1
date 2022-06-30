package com.any1.chat.interfaces

import com.any1.chat.models.RequestModel

interface RequestListListener {
    fun showRequestList(arrayList: ArrayList<RequestModel>)
}