package com.any1.chat.interfaces

import com.any1.chat.models.MemberModel

interface OnMemberClickListener {
    fun onGroupMemberClicked(position : Int, memberArrayList : ArrayList<MemberModel>)
}