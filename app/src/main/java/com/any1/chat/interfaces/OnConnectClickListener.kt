package com.any1.chat.interfaces

import com.any1.chat.models.MemberModel

interface OnConnectClickListener {
    fun onConnectClicked(position : Int , membersArrayList: ArrayList<MemberModel>)
}