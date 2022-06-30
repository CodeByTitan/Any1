package com.any1.chat.interfaces

import com.any1.chat.models.MemberModel

interface OnMenuClickListener {
    fun onMenuClicked(position : Int, memberModeList : ArrayList<MemberModel>)
}