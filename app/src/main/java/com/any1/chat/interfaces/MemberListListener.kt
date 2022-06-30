package com.any1.chat.interfaces

import com.any1.chat.models.MemberModel

interface MemberListListener {
    fun showMemberList(arrayList: ArrayList<MemberModel>)
}